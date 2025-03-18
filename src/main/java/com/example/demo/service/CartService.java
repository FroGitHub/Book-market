package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.cart.CartItemCreateRequestDto;
import com.example.demo.dto.cart.CartItemCreateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface CartService {
    Page<CartDto> getCarts(Authentication authentication, Pageable pageable);

    void addCartItem(Authentication authentication,
                     CartItemCreateRequestDto createItemRequestDto);

    CartItemCreateResponseDto updateCartItem(Authentication authentication,
                                             Long cartItemId,
                                             CartItemCreateRequestDto createItemRequestDto);

    void deleteCart(Long cartItemId);
}
