package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Long cartId; // Thay vì CartDTO để tránh vòng lặp
    private Long productId; // Nếu chỉ cần ID, tránh serialize quá nhiều dữ liệu
    private Integer quantity;
    private double discount;
    private double productPrice;

    public double getFinalPrice() {
        return productPrice - (productPrice * discount / 100);
    }
}
