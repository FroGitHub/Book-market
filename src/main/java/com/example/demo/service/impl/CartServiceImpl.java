package com.example.demo.service.impl;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.cart.CartItemCreateRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemsMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cart.CartItemRepository;
import com.example.demo.repository.cart.CartRepository;
import com.example.demo.service.CartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemsMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public CartDto getCarts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartMapper.toDto(
                cartRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No cart with user id: " + user.getId()
                        ))
        );
    }

    @Override
    @Transactional
    public CartDto addCartItem(Authentication authentication,
                               CartItemCreateRequestDto createItemRequestDto) {
        User user = (User) authentication.getPrincipal();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user’s cart with id: " + user.getId()));

        Book book = bookRepository.findById(createItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + createItemRequestDto.getBookId())
                );

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + createItemRequestDto.getQuantity());
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setBook(book);
            newCartItem.setQuantity(createItemRequestDto.getQuantity());
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public CartDto updateCartItem(Authentication authentication, Long cartItemId,
                                  CartItemCreateRequestDto createItemRequestDto) {
        User user = (User) authentication.getPrincipal();

        CartItem cartItem = cartItemRepository
                .findByIdAndCartUserId(cartItemId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No cart item with id: " + cartItemId));

        cartItemMapper.updateCartItem(cartItem, createItemRequestDto);
        cartItemRepository.save(cartItem);

        return cartMapper.toDto(cartItem.getCart());
    }

    @Override
    public void deleteCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
