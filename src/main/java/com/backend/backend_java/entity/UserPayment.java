package com.backend.backend_java.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;
    private Double paymentAmount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    private String fullname;
    private String address;
    private String phone;

    @ManyToOne
    private PaymentMethod paymentMethod;

    @ManyToOne
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "user_id") // đặt tên cột mong muốn
    private User user;

    // Getters and setters
}
