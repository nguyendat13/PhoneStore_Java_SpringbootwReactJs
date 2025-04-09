package com.backend.backend_java.entity;

import java.time.LocalDateTime;

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

    private String transactionId;
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
    private User user;

    // Getters and setters
}
