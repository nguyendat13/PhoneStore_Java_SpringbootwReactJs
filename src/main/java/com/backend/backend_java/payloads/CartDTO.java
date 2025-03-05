package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private Double totalPrice;
    private String updateAt;
    private Long userId; // Chỉ lấy ID của user
    private List<CartItemDTO> cartItems; // Danh sách sản phẩm trong giỏ hàng
}
