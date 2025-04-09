package com.backend.backend_java.service.impl;

import com.backend.backend_java.entity.Address;
import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.CartItem;
import com.backend.backend_java.entity.PaymentMethod;
import com.backend.backend_java.entity.PaymentStatus;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.entity.UserPayment;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.UserPaymentDTO;
import com.backend.backend_java.repository.CartItemRepo;
import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.PaymentMethodRepo;
import com.backend.backend_java.repository.PaymentStatusRepo;
import com.backend.backend_java.repository.ProductRepo;
import com.backend.backend_java.repository.UserPaymentRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.CartService;
import com.backend.backend_java.service.OrderService;
import com.backend.backend_java.service.PaymentService;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PaymentMethodRepo paymentMethodRepo;

    @Autowired
    private PaymentStatusRepo paymentStatusRepo;

    @Autowired
    private UserPaymentRepo userPaymentRepo;

    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartService cartService;

    @Override
    public PaymentMethod createPaymentMethod(String name) {
        if (paymentMethodRepo.findByName(name).isPresent()) {
            throw new RuntimeException("Phương thức thanh toán đã tồn tại");
        }
        PaymentMethod method = new PaymentMethod();
        method.setName(name);
        return paymentMethodRepo.save(method);
    }

    @Override
    public PaymentStatus createPaymentStatus(String name) {
        if (paymentStatusRepo.findByName(name).isPresent()) {
            throw new RuntimeException("Trạng thái thanh toán đã tồn tại");
        }

        PaymentStatus status = new PaymentStatus();
        status.setName(name);
        return paymentStatusRepo.save(status);
    }

    @Override
    @Transactional
    public UserPaymentDTO checkout(UserPaymentDTO dto) {
        // 1. Lấy thông tin user từ ID
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", dto.getUserId()));

        // 2. Lấy giỏ hàng từ user
        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new APIException("Giỏ hàng trống, không thể thanh toán.");
        }

        // 3. Tính tổng tiền
        double totalAmount = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        // 4. Lấy địa chỉ đầu tiên
        Address address = user.getAddresses().isEmpty() ? null : user.getAddresses().get(0);
        if (address == null) {
            throw new APIException("Không tìm thấy địa chỉ người dùng.");
        }

        String fullAddress = address.getBuildingName() + ", " + address.getStreet() + ", " + address.getCity() +
                ", " + address.getState() + ", " + address.getCountry() + " - " + address.getPincode();

        // 5. Tạo UserPayment
        UserPayment payment = new UserPayment();
        payment.setUser(user);
        payment.setFullname(user.getFullname());
        payment.setPhone(user.getPhone());
        payment.setAddress(fullAddress);
        long transactionId = System.currentTimeMillis() + (long) (Math.random() * 1000);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentAmount(totalAmount);

        // 6. Gán phương thức thanh toán
        PaymentMethod method = paymentMethodRepo.findById(dto.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", dto.getPaymentMethodId()));
        payment.setPaymentMethod(method);

        // 7. Gán trạng thái thanh toán
        PaymentStatus status = paymentStatusRepo.findById(dto.getPaymentStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("PaymentStatus", "id", dto.getPaymentStatusId()));
        payment.setPaymentStatus(status);

        // 8. Lưu vào DB
        UserPayment saved = userPaymentRepo.save(payment);

        // 9. Trừ số lượng tồn kho
        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product);
        }

        // ❌ KHÔNG tạo đơn hàng và KHÔNG xoá giỏ hàng
        return modelMapper.map(saved, UserPaymentDTO.class);
    }
}
