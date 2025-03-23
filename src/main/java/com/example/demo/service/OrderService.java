package com.example.demo.service;

import com.example.demo.dto.order.OrderCreateRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.OrderItemDto;
import com.example.demo.dto.order.OrderStatusPatchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    Page<OrderDto> getOrders(Authentication authentication,
                             Pageable pageable);

    OrderDto createOrder(Authentication authentication,
                         OrderCreateRequestDto requestDto);

    OrderDto setNewStatus(Long id,
                          OrderStatusPatchDto statusPatchDto);

    Page<OrderItemDto> getOrderItems(Authentication authentication,
                                     Pageable pageable, Long id);

    OrderItemDto getOrderItem(Authentication authentication,
                              Long orderId,
                              Long itemId);
}
