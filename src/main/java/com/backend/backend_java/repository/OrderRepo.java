package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.backend.backend_java.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUser_UserId(Long userId);

    @EntityGraph(attributePaths = { "orderItems", "orderItems.product" })
    Optional<Order> findByOrderId(Long orderId);

}
