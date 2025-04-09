package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend_java.entity.Order;

public interface OrderRepo extends JpaRepository<Order,Long> {

}
