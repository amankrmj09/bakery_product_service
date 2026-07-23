package com.blubugtech.bakery_product_service.entity;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "tax_rates")
public class TaxRate {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Tax type name is required")
    @Size(min = 2, max = 50, message = "Tax type must be between 2 and 50 characters")
    private String type; // e.g., "Standard", "Zero", "Reduced"

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.00", message = "Tax rate cannot be negative")
    @Digits(integer = 2, fraction = 4, message = "Invalid tax rate format")
    private BigDecimal rate; // e.g., 0.08 for 8%

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
