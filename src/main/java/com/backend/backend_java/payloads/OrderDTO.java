package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String orderStatus;
    private String orderMethod;
    private Double totalAmount;
    private String orderDate;
    private String fullname;
    private String address;
    private String phone;
    private Long userId; // Chỉ lấy ID của User
    private List<OrderItemDTO> orderItems; // Danh sách sản phẩm trong đơn hàng
}
