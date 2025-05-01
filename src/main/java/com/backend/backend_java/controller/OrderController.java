package com.backend.backend_java.controller;

import com.backend.backend_java.entity.UserPayment;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.payloads.OrderItemDTO;
import com.backend.backend_java.payloads.OrderUpdateDTO;
import com.backend.backend_java.repository.UserPaymentRepo;
import com.backend.backend_java.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserPaymentRepo userPaymentRepo;

    @PutMapping("/{orderId}/update")
    public ResponseEntity<?> updateOrderAndItems(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateDTO orderUpdateDTO) {

        orderService.updateOrderAndItems(orderId, orderUpdateDTO);
        return ResponseEntity.ok("Cập nhật đơn hàng và sản phẩm thành công!");
    }

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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO dto = orderService.getOrderById(orderId);
        if (dto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // ✅ API: Lấy danh sách tất cả đơn hàng (dành cho admin)
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
