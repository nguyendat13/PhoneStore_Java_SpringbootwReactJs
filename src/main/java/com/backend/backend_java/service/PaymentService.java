package com.backend.backend_java.service;

import com.backend.backend_java.entity.PaymentMethod;
import com.backend.backend_java.entity.PaymentStatus;
import com.backend.backend_java.payloads.UserPaymentDTO;

public interface PaymentService {

    PaymentMethod createPaymentMethod(String name);

    PaymentStatus createPaymentStatus(String name);

    UserPaymentDTO checkout(UserPaymentDTO dto);
}
