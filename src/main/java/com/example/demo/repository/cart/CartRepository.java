package com.example.demo.repository.cart;

import com.example.demo.model.Cart;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findByUserEmail(Pageable pageable, String email);

    Optional<Cart> findByUserEmail(String email);
}
