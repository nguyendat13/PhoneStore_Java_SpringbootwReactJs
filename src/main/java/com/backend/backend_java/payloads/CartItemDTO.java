package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Double productPrice;
    private Double discount;
    private Integer quantity;
    private Long productId; // Chỉ lấy ID của sản phẩm
    private String productName; // Tên sản phẩm
    private String productImage; // Ảnh sản phẩm
}
