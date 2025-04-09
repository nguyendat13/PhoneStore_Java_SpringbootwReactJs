package com.backend.backend_java.controller;

import com.backend.backend_java.entity.UserPayment;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.repository.UserPaymentRepo;
import com.backend.backend_java.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserPaymentRepo userPaymentRepo;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId) {
        // Lấy payment mới nhất của user (có thể sửa điều kiện theo ý bạn)
        UserPayment payment = userPaymentRepo
                .findTopByUser_UserIdOrderByPaymentDateDesc(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thanh toán gần nhất."));

        OrderDTO orderDTO = orderService.createOrderFromCart(userId, payment);
        return ResponseEntity.ok(orderDTO);
    }

}
