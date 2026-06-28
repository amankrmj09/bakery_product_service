package com.shah_s.bakery_product_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RefreshScope
public class AwsS3Service {

    private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);

    private final S3Client s3Client;
    private final String bucketName;
    private final String regionStr;
    private final String publicDomainUrl;

    public AwsS3Service(
            @Value("${aws.credentials.access-key:mock-access-key}") String accessKey,
            @Value("${aws.credentials.secret-key:mock-secret-key}") String secretKey,
            @Value("${aws.region:us-east-1}") String region,
            @Value("${aws.s3.bucket-name:mock-bucket}") String bucketName,
            @Value("${aws.s3.endpoint:#{null}}") String endpoint,
            @Value("${aws.s3.public-url:#{null}}") String publicDomainUrl) {
        
        this.bucketName = bucketName;
        this.regionStr = region;
        this.publicDomainUrl = publicDomainUrl;
        
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials));

        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
            // For custom S3 implementations like Garage or MinIO, path style access is usually required
            builder.forcePathStyle(true);
        }

        this.s3Client = builder.build();
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        
        String fileName = UUID.randomUUID().toString() + extension;
        
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url;
            if (publicDomainUrl != null && !publicDomainUrl.isEmpty()) {
                String baseUrl = publicDomainUrl.endsWith("/") ? publicDomainUrl : publicDomainUrl + "/";
                url = baseUrl + fileName;
            } else {
                url = s3Client.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build()).toExternalForm();
            }
            logger.info("Successfully uploaded file to S3, returning URL: {}", url);
            return url;
            
        } catch (IOException e) {
            logger.error("Failed to upload file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                urls.add(uploadFile(file));
            }
        }
        return urls;
    }

    public byte[] getFile(String fileName) {
        try (var inputStream = s3Client.getObject(
                software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build()
        )) {
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching file from S3", e);
        }
    }
}
