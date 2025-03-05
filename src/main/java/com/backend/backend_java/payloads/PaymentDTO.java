package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private Double paymentAmount;
    private String paymentDate;
    private String fullname;
    private String address;
    private String phone;
    private Long userId; // Chỉ lấy ID của User
    private Long cartId; // Chỉ lấy ID của Cart
}
