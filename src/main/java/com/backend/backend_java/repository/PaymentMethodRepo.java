package com.backend.backend_java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend_java.entity.PaymentMethod;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByName(String name);

}
