package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Long favoriteId;
    private Long userId; // Chỉ lấy ID của User
    private Long productId; // Chỉ lấy ID của Product
    private LocalDateTime createdAt;

    private String productName;
    private String image;
    private double price;
    private double priceSale;
}
