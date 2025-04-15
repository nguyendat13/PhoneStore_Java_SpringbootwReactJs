package com.backend.backend_java.payloads;

import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;
    private Double totalPrice = 0.0;
    private List<CartItemDTO> cartItems; // ✅ Thay vì products, ta lưu CartItem
    private String email; // Lấy từ User
    // Hàm tính tổng tiền giỏ hàng

    public void calculateTotalPrice() {
        if (cartItems != null) {
            this.totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getFinalPrice() * item.getQuantity())
                    .sum();
        } else {
            this.totalPrice = 0.0;
        }
    }

}