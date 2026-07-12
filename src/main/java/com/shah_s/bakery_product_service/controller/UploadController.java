package com.shah_s.bakery_product_service.controller;

import com.shah_s.bakery_product_service.service.R2StorageService;
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

    private final R2StorageService r2StorageService;

    public UploadController(R2StorageService r2StorageService) {
        this.r2StorageService = r2StorageService;
    }

    @PostMapping(value = "/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadMedia(
            @RequestPart("media") List<MultipartFile> media) {
        
        logger.info("Upload media request received, count: {}", media.size());

        List<String> urls = r2StorageService.uploadFiles(media);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Files uploaded successfully");
        response.put("urls", urls);

        logger.info("Successfully uploaded {} media files", urls.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/media/{fileName}")
    public ResponseEntity<byte[]> getMedia(@PathVariable String fileName) {
        byte[] data = r2StorageService.getFile(fileName);
        org.springframework.http.MediaType mediaType = org.springframework.http.MediaTypeFactory
            .getMediaType(fileName).orElse(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
            .contentType(mediaType)
            .body(data);
    }
}
