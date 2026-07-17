package com.blubugtech.bakery_product_service.integration.storage;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface StorageService {
    String resolveUrl(String key);
    String uploadFile(MultipartFile file);
    String uploadFile(MultipartFile file, String folder);
    List<String> uploadFiles(List<MultipartFile> files);
    void delete(String url);
    byte[] getFile(String fileName);
}
