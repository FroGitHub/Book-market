package com.example.demo.repository.cart;

import com.example.demo.model.CartItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndCartUserId(Long cartItemId, Long userId);

    Set<CartItem> findByCartUserId(Long id);
}
