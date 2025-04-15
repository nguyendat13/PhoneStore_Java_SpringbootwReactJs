package com.backend.backend_java.service;

import com.backend.backend_java.entity.UserPayment;
import com.backend.backend_java.payloads.OrderDTO;
import java.util.List;

public interface OrderService {
    OrderDTO createOrderFromCart(Long userId, UserPayment payment);

    List<OrderDTO> getOrdersByUserId(Long userId);
}
