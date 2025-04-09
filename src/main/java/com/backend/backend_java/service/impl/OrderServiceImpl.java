package com.backend.backend_java.service.impl;

import com.backend.backend_java.entity.*;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.payloads.OrderItemDTO;
import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.OrderItemRepo;
import com.backend.backend_java.repository.OrderRepo;
import com.backend.backend_java.repository.ProductRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;

    @Override
    public OrderDTO createOrderFromCart(Long userId, UserPayment payment) {
        User user = userRepo.findById(userId).orElseThrow(() -> new APIException("User not found"));
        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new APIException("Giỏ hàng trống hoặc không tồn tại");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus("Đã thanh toán");
        order.setOrderDate(LocalDate.now().toString());
        order.setFullname(payment.getFullname()); // lấy từ payment
        order.setAddress(payment.getAddress());
        order.setPhone(payment.getPhone());
        order.setTotalAmount(cart.getTotalPrice());

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setQuantity(cartItem.getQuantity());
            item.setDiscount(cartItem.getDiscount());
            item.setOrderedProductPrice(cartItem.getProduct().getPrice());
            item.setPaymentStatus(payment.getPaymentStatus().getName());
            item.setPaymentMethod(payment.getPaymentMethod().getName());
            item.setProduct(cartItem.getProduct());
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        // ✅ Xoá giỏ hàng sau khi tạo đơn
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepo.save(cart);

        // Convert to DTO
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        List<OrderItemDTO> itemDTOs = orderItems.stream().map(item -> {
            OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
            dto.setProductId(item.getProduct().getProductId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setProductImage(item.getProduct().getImage());
            return dto;
        }).collect(Collectors.toList());
        orderDTO.setOrderItems(itemDTOs);

        return orderDTO;
    }

}
