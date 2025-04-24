package com.backend.backend_java.controller;

import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.PaymentMethod;
import com.backend.backend_java.entity.PaymentStatus;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.UserPaymentDTO;
import com.backend.backend_java.payloads.request.QRCodeGenerator;
import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.PaymentService;
import com.google.zxing.WriterException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/generateQRCode")
    public ResponseEntity<Void> generateQRCode(@RequestParam String paymentData, HttpServletResponse response) {
        try {
            // Tạo QR code và nhận ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = QRCodeGenerator.generateQRCode(paymentData);

            // Đặt tiêu đề cho response để gửi ảnh về client
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=\"qr_code.png\"");

            // Ghi dữ liệu QR code vào response output stream
            ServletOutputStream out = response.getOutputStream();
            byteArrayOutputStream.writeTo(out);
            out.flush();

            return ResponseEntity.ok().build(); // Trả về mã trạng thái thành công
        } catch (WriterException | IOException e) {
            // Xử lý lỗi khi tạo QR code
            return ResponseEntity.status(500).build();
        }
    }

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

    @DeleteMapping("/public/payment-cancel/{paymentId}")
    public ResponseEntity<String> cancelAndDeletePayment(@PathVariable Long paymentId) {
        try {
            // Gọi method để hủy và xóa thanh toán khỏi database
            paymentService.cancelAndDeletePayment(paymentId);
            return ResponseEntity.ok("Thanh toán đã được hủy và xóa khỏi hệ thống.");
        } catch (ResourceNotFoundException e) {
            // Trường hợp không tìm thấy thanh toán với paymentId
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy thanh toán với ID: " + paymentId);
        } catch (Exception e) {
            // Trường hợp có lỗi hệ thống khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi hủy thanh toán: " + e.getMessage());
        }
    }

    // Lấy danh sách thanh toán theo userId
    @GetMapping("/public/payments/user/{userId}")
    public ResponseEntity<List<UserPaymentDTO>> getPaymentsByUser(@PathVariable Long userId) {
        // Gọi service để lấy danh sách thanh toán theo userId
        List<UserPaymentDTO> payments = paymentService.getPaymentsByUserId(userId);
        // Trả về danh sách thanh toán trong response
        return ResponseEntity.ok(payments);
    }

    // Lấy tất cả thanh toán (admin)
    @GetMapping("/admin/payments")
    public ResponseEntity<List<UserPaymentDTO>> getAllPayments() {
        // Gọi service để lấy tất cả thanh toán
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Lấy tất cả PaymentMethods (admin)
    @GetMapping("/public/payments/methods")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        List<PaymentMethod> methods = paymentService.getAllPaymentMethods();
        return ResponseEntity.ok(methods);
    }

    // Lấy tất cả PaymentStatuses (admin)
    @GetMapping("/public/payments/statuses")
    public ResponseEntity<List<PaymentStatus>> getAllPaymentStatuses() {
        List<PaymentStatus> statuses = paymentService.getAllPaymentStatuses();
        return ResponseEntity.ok(statuses);
    }

    // Xóa PaymentMethod (admin)
    @DeleteMapping("/admin/payments/methods/{methodId}")
    public ResponseEntity<String> deletePaymentMethod(@PathVariable Long methodId) {
        try {
            paymentService.deletePaymentMethod(methodId);
            return ResponseEntity.ok("Phương thức thanh toán đã được xóa.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy phương thức thanh toán với ID: " + methodId);
        }
    }

    // Xóa PaymentStatus (admin)
    @DeleteMapping("/admin/payments/statuses/{statusId}")
    public ResponseEntity<String> deletePaymentStatus(@PathVariable Long statusId) {
        try {
            paymentService.deletePaymentStatus(statusId);
            return ResponseEntity.ok("Trạng thái thanh toán đã được xóa.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy trạng thái thanh toán với ID: " + statusId);
        }
    }
}
