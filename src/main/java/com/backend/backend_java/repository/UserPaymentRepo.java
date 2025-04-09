package com.backend.backend_java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend_java.entity.UserPayment;

public interface UserPaymentRepo extends JpaRepository<UserPayment, Long> {
    Optional<UserPayment> findTopByUser_UserIdOrderByPaymentDateDesc(Long userId);

}
