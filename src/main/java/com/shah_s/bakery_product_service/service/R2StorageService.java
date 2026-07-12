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
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RefreshScope
public class R2StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(R2StorageService.class);

    private final S3Client s3Client;
    private final String bucketName;
    private final String cdnBaseUrl;

    public R2StorageService(
            @Value("${r2.access-key:mock-access-key}") String accessKey,
            @Value("${r2.secret-key:mock-secret-key}") String secretKey,
            @Value("${r2.bucket:mock-bucket}") String bucketName,
            @Value("${r2.endpoint:#{null}}") String endpoint,
            @Value("${r2.cdn-base-url:#{null}}") String cdnBaseUrl) {
        
        this.bucketName = bucketName;
        this.cdnBaseUrl = (cdnBaseUrl != null && cdnBaseUrl.endsWith("/")) ? cdnBaseUrl : cdnBaseUrl + "/";
        
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials));

        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
            // R2 usually supports virtual hosted style, but some S3 compatible storage requires path style
            // Cloudflare R2 works well with the default virtual hosted style in AWS SDK v2, 
            // but we can leave path style off or on depending on standard R2 usage. 
            // We'll leave it as default (virtual hosted style).
        }

        this.s3Client = builder.build();
    }

    public String resolveUrl(String key) {
        if (key == null || key.isBlank()) return null;
        if (key.startsWith("http://") || key.startsWith("https://")) return key;
        return cdnBaseUrl + key;
    }

    public String uploadFile(MultipartFile file) {
        return uploadFile(file, "");
    }
    
    public String uploadFile(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        
        String fileName = UUID.randomUUID().toString() + extension;
        String key = (folder == null || folder.isEmpty()) ? fileName : folder + "/" + fileName;
        
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url = cdnBaseUrl + key;
            LOG.info("Successfully uploaded file to R2, returning URL: {}", url);
            return url;
            
        } catch (IOException e) {
            LOG.error("Failed to upload file to R2", e);
            throw new RuntimeException("Failed to upload file to R2", e);
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

    public void delete(String url) {
        if (url == null || url.isBlank() || !url.startsWith(cdnBaseUrl)) {
            return;
        }
        try {
            String key = url.substring(cdnBaseUrl.length());
            
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            s3Client.deleteObject(request);
            LOG.info("Successfully deleted file from R2: {}", key);
        } catch (Exception e) {
            LOG.error("Failed to delete file from R2: {}", url, e);
        }
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
            throw new RuntimeException("Error fetching file from R2", e);
        }
    }
}
