package com.backend.backend_java.controller;

import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.PaymentMethod;
import com.backend.backend_java.entity.PaymentStatus;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.UserPaymentDTO;
import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/public/payments/methods")
    public ResponseEntity<PaymentMethod> addPaymentMethod(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        return new ResponseEntity<>(paymentService.createPaymentMethod(name), HttpStatus.CREATED);
    }

    @PostMapping("/public/payments/statuses")
    public ResponseEntity<PaymentStatus> addPaymentStatus(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        return new ResponseEntity<>(paymentService.createPaymentStatus(name), HttpStatus.CREATED);
    }

    @PostMapping("/public/checkout")
    public ResponseEntity<UserPaymentDTO> checkout(@RequestBody UserPaymentDTO userPaymentDTO) {
        UserPaymentDTO savedPayment = paymentService.checkout(userPaymentDTO);
        return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
    }

}
