package com.example.demo.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemCreateRequestDto {
    private Long bookId;
    @Positive
    private int quantity;
}
