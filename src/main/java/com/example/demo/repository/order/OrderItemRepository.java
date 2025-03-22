package com.example.demo.repository.order;

import com.example.demo.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findByOrderIdAndOrderUserId(Pageable pageable,
                                                Long id,
                                                Long userId);

    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id,
                                                         Long orderId,
                                                         Long userId);
}
