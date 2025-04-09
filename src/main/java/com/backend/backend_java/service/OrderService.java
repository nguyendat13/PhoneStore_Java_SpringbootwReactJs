package com.backend.backend_java.service;

import com.backend.backend_java.entity.UserPayment;
import com.backend.backend_java.payloads.OrderDTO;

public interface OrderService {
    OrderDTO createOrderFromCart(Long userId, UserPayment payment);
}
