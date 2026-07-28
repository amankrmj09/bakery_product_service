package com.blubugtech.bakery_product_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.blubugtech.common.exception.handler.ErrorResponse;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.blubugtech.common.exception.handler.BaseExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(ProductServiceException ex, WebRequest request) {
        logger.error("Product service error: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "PRODUCT_SERVICE_ERROR",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );

        return ResponseEntity.badRequest().body(error);
    }

    

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex, WebRequest request) {
        logger.error("Insufficient stock error: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            "INSUFFICIENT_STOCK",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );

        if (ex.getProductId() != null) {
            Map<String, Object> details = new HashMap<>();
            details.put("productId", ex.getProductId());
            details.put("requestedQuantity", ex.getRequestedQuantity());
            details.put("availableQuantity", ex.getAvailableQuantity());
            error.setDetails(details);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    

    

    

    // Error Response Class
    
    
    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantityException(InvalidQuantityException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("INVALID_QUANTITY", ex.getMessage(), LocalDateTime.now(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(org.springframework.web.multipart.MaxUploadSizeExceededException exc, WebRequest request) {
        logger.error("Upload size exceeded: {}", exc.getMessage());
        ErrorResponse error = new ErrorResponse(
            "PAYLOAD_TOO_LARGE",
            "File size exceeds the configured maximum limit (50MB).",
            LocalDateTime.now(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }

    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(org.springframework.web.multipart.MultipartException exc, WebRequest request) {
        logger.error("Multipart error: {}", exc.getMessage());
        ErrorResponse error = new ErrorResponse(
            "MULTIPART_ERROR",
            "Failed to parse multipart request, possibly due to file size exceeding limits.",
            LocalDateTime.now(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}

