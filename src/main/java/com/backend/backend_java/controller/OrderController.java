package com.backend.backend_java.controller;

import com.backend.backend_java.entity.UserPayment;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.repository.UserPaymentRepo;
import com.backend.backend_java.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/order")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        // Lấy danh sách đơn hàng của người dùng
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);

        // Kiểm tra nếu không có đơn hàng nào, trả về 404 Not Found
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build(); // Hoặc ResponseEntity.notFound().build();
        }

        // Trả về danh sách đơn hàng với mã 200 OK
        return ResponseEntity.ok(orders);
    }

}
