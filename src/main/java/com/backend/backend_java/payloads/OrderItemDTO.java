package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Double orderedProductPrice;
    private Double discount;
    private Integer quantity;
    // private String paymentStatus;
    // private String paymentMethod;
    private Long productId; // Chỉ lấy ID sản phẩm
    private String productName; // Lấy thêm tên sản phẩm
    private String productImage; // Lấy ảnh sản phẩm
}
