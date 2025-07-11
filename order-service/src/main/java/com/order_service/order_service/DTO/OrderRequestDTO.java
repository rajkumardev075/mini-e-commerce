package com.order_service.order_service.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

public class OrderRequestDTO {
    @NotBlank( message = "Product Id is required")
    private String productId;

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @NotNull
    @Min(value = 1)
    private Integer quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
