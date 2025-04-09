package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend_java.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}
