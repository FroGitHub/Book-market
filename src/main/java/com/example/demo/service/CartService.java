package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.cart.CartItemCreateRequestDto;
import org.springframework.security.core.Authentication;

public interface CartService {
    CartDto getCarts(Authentication authentication);

    CartDto addCartItem(Authentication authentication,
                     CartItemCreateRequestDto createItemRequestDto);

    CartDto updateCartItem(Authentication authentication,
                           Long cartItemId,
                           CartItemCreateRequestDto createItemRequestDto);

    void deleteCart(Long cartItemId);
}
