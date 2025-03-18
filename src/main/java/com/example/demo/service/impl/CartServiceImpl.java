package com.example.demo.service.impl;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.cart.CartItemCreateRequestDto;
import com.example.demo.dto.cart.CartItemCreateResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemsMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.User;
import com.example.demo.repository.cart.CartItemRepository;
import com.example.demo.repository.cart.CartRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;

    @Override
    public Page<CartDto> getCarts(Authentication authentication, Pageable pageable) {
        return cartRepository
                .findByUserEmail(pageable, authentication.getName())
                .map(cartMapper::toDto);
    }

    @Override
    public void addCartItem(Authentication authentication,
                            CartItemCreateRequestDto createItemRequestDto) {

        String email = authentication.getName();
        Cart cart = cartRepository.findByUserEmail(email).orElseGet(() -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("No user with email: " + email));
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        CartItem cartItem = cartItemMapper.toModel(createItemRequestDto);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);
        cartItemMapper.toResponseDto(cartItem);
    }

    @Override
    public CartItemCreateResponseDto updateCartItem(Authentication authentication,
                                                    Long cartItemId,
                                                    CartItemCreateRequestDto createItemRequestDto) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No cart item with id: " + cartItemId));

        cartItemMapper.updateCartItem(cartItem, createItemRequestDto);
        return cartItemMapper.toResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
