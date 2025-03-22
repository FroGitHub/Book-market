package com.example.demo.dto.order;

import com.example.demo.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusPatchDto {
    @NotNull
    private Order.Status status;
}
