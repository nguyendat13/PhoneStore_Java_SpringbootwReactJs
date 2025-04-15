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
import jakarta.transaction.Transactional;

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
        // Tính tổng giá trị đơn hàng sau giảm giá
        double totalAmount = cart.getCartItems().stream().mapToDouble(cartItem -> {
            // Tính giá trị sản phẩm sau giảm giá
            double discountedPrice = cartItem.getProduct().getPrice() * (1 - cartItem.getDiscount() / 100);
            return discountedPrice * cartItem.getQuantity();
        }).sum();

        // Cập nhật tổng giá trị vào order
        order.setTotalAmount(totalAmount);

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

        // Lưu đơn hàng vào database
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

    @Transactional
    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        // Lấy danh sách đơn hàng của người dùng từ repository
        List<Order> orders = orderRepo.findByUser_UserId(userId);

        // Chuyển đổi các đơn hàng thành OrderDTO
        return orders.stream().map(order -> {
            // Tính tổng giá trị của đơn hàng sau khi áp dụng giảm giá
            Double totalAmount = order.getOrderItems().stream()
                    .mapToDouble(item -> {
                        // Tính giá trị cuối cùng của sản phẩm sau khi áp dụng giảm giá
                        double discountedPrice = item.getOrderedProductPrice() * (1 - item.getDiscount() / 100);
                        return discountedPrice * item.getQuantity();
                    })
                    .sum();
            // Chuyển đổi Order sang OrderDTO
            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            // Cập nhật lại tổng giá trị cho OrderDTO
            orderDTO.setTotalAmount(totalAmount);
            // Chuyển đổi OrderItems thành OrderItemDTO và thêm thông tin sản phẩm
            List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
                OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
                dto.setProductId(item.getProduct().getProductId());
                dto.setProductName(item.getProduct().getProductName());
                dto.setProductImage(item.getProduct().getImage());
                return dto;
            }).collect(Collectors.toList());

            // Gán danh sách OrderItemDTO vào OrderDTO
            orderDTO.setOrderItems(itemDTOs);

            return orderDTO;
        }).collect(Collectors.toList());
    }

}
