package com.blubugtech.bakery_product_service.controller;

import com.blubugtech.bakery_product_service.integration.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private final StorageService storageService;

    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<com.blubugtech.bakery_product_service.dto.media.MediaUploadResponse> uploadMedia(
            @RequestPart("media") List<MultipartFile> media) {
        
        logger.info("Upload media request received, count: {}", media.size());

        List<String> urls = storageService.uploadFiles(media);
        
        com.blubugtech.bakery_product_service.dto.media.MediaUploadResponse response = new com.blubugtech.bakery_product_service.dto.media.MediaUploadResponse("Files uploaded successfully", urls);

        logger.info("Successfully uploaded {} media files", urls.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/media/{fileName}")
    public ResponseEntity<byte[]> getMedia(@PathVariable String fileName) {
        byte[] data = storageService.getFile(fileName);
        org.springframework.http.MediaType mediaType = org.springframework.http.MediaTypeFactory
            .getMediaType(fileName).orElse(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
            .contentType(mediaType)
            .body(data);
    }
}
