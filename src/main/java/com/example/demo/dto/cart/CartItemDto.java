package com.example.demo.dto.cart;

public record CartItemDto(Long id,
                           Long bookId,
                           String bookTitle,
                           int quantity) {

}
