package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend_java.entity.UserPayment;

public interface UserPaymentRepo extends JpaRepository<UserPayment, Long> {}
