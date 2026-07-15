package com.shah_s.bakery_product_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.devofblue.common.exception.ErrorResponseDto;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.devofblue.common.exception.BaseExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ErrorResponseDto> handleProductServiceException(ProductServiceException ex, WebRequest request) {
        logger.error("Product service error: {}", ex.getMessage());

        ErrorResponseDto error = new ErrorResponseDto(
            "PRODUCT_SERVICE_ERROR",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );

        return ResponseEntity.badRequest().body(error);
    }

    

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDto> handleInsufficientStockException(InsufficientStockException ex, WebRequest request) {
        logger.error("Insufficient stock error: {}", ex.getMessage());

        ErrorResponseDto error = new ErrorResponseDto(
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
    public ResponseEntity<ErrorResponseDto> handleInvalidQuantityException(InvalidQuantityException ex, WebRequest request) {
        ErrorResponseDto error = new ErrorResponseDto("INVALID_QUANTITY", ex.getMessage(), LocalDateTime.now(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    


}

