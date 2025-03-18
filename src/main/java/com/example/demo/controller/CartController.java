package com.example.demo.controller;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.cart.CartItemCreateRequestDto;
import com.example.demo.dto.cart.CartItemCreateResponseDto;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "Cart controller", description = "Users can add items and remove it")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get cart",
            description = "Returns authorized user`s cart with list of items")
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<CartDto> getCarts(Authentication authentication, Pageable pageable) {
        return cartService.getCarts(authentication, pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void createCart(Authentication authentication,
                           @RequestBody @Valid CartItemCreateRequestDto createItemRequestDto) {
        cartService.addCartItem(authentication, createItemRequestDto);
    }

    @PutMapping("/item/{cartItemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CartItemCreateResponseDto updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemCreateRequestDto createItemRequestDto
    ) {
        return cartService.updateCartItem(authentication, cartItemId, createItemRequestDto);
    }

    @DeleteMapping("/item/{cartItemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCart(cartItemId);
    }
}
