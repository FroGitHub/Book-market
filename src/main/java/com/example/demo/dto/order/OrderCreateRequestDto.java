package com.example.demo.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderCreateRequestDto {
    @NotBlank
    private String shippingAddress;
}
