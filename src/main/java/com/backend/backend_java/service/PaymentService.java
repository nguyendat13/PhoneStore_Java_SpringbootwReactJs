package com.backend.backend_java.service;

import com.backend.backend_java.entity.PaymentMethod;
import com.backend.backend_java.entity.PaymentStatus;
import com.backend.backend_java.payloads.UserPaymentDTO;
import java.util.List;

public interface PaymentService {

    PaymentMethod createPaymentMethod(String name);

    PaymentStatus createPaymentStatus(String name);

    UserPaymentDTO checkout(UserPaymentDTO dto);

    public void cancelAndDeletePayment(Long paymentId);

    List<UserPaymentDTO> getAllPayments();

    List<UserPaymentDTO> getPaymentsByUserId(Long userId);

    List<PaymentMethod> getAllPaymentMethods(); // Thêm phương thức lấy tất cả PaymentMethods

    List<PaymentStatus> getAllPaymentStatuses(); // Thêm phương thức lấy tất cả PaymentStatuses

    void deletePaymentMethod(Long methodId); // Thêm phương thức xóa PaymentMethod

    void deletePaymentStatus(Long statusId); // Thêm phương thức xóa PaymentStatus
}
