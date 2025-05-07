package com.backend.backend_java.payloads;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentDTO {
    private Long id; // Thêm paymentId

    private Long transactionId;
    private Double paymentAmount;

    private LocalDateTime paymentDate;

    private String fullname;
    private String email;

    private String address;
    private String phone;

    private Long userId;
    private Long paymentMethodId;

    private Long paymentStatusId;

    // Getters và Setters đầy đủ
}
