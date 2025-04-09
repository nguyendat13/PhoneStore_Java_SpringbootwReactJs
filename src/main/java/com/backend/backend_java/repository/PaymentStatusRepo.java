package com.backend.backend_java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend_java.entity.PaymentStatus;

public interface PaymentStatusRepo extends JpaRepository<PaymentStatus, Long> {

    Optional<PaymentStatus> findByName(String name);

}
