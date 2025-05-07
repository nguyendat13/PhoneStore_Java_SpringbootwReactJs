package com.backend.backend_java.service.impl;

import com.backend.backend_java.entity.*;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.payloads.OrderItemDTO;
import com.backend.backend_java.payloads.OrderUpdateDTO;
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
import java.util.Optional;
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
        order.setOrderStatus("Đang xử lý");
        order.setPaymentMethod(payment.getPaymentMethod().getName());
        order.setOrderDate(LocalDate.now().toString());
        order.setFullname(payment.getFullname()); // lấy từ payment
        order.setEmail(payment.getEmail());
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

    @Transactional
    @Override
    public List<OrderDTO> getAllOrders() {
        // Lấy tất cả đơn hàng từ database
        List<Order> orders = orderRepo.findAll();

        // Lọc đơn hàng: chỉ lấy của user có role USER
        List<Order> userOrders = orders.stream()
                .filter(order -> order.getUser() != null
                        && order.getUser().getRoles().stream()
                                .anyMatch(role -> "USER".equals(role.getRoleName())))
                .collect(Collectors.toList());

        // Chuyển từng đơn hàng sang OrderDTO và tính lại tổng tiền sau giảm giá
        return userOrders.stream().map(order -> {
            Double totalAmount = order.getOrderItems().stream()
                    .mapToDouble(item -> {
                        double discountedPrice = item.getOrderedProductPrice() * (1 - item.getDiscount() / 100);
                        return discountedPrice * item.getQuantity();
                    }).sum();

            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            orderDTO.setTotalAmount(totalAmount);

            List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
                OrderItemDTO dto = modelMapper.map(item, OrderItemDTO.class);
                dto.setProductId(item.getProduct().getProductId());
                dto.setProductName(item.getProduct().getProductName());
                dto.setProductImage(item.getProduct().getImage());
                return dto;
            }).collect(Collectors.toList());

            orderDTO.setOrderItems(itemDTOs);

            return orderDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> orderOpt = orderRepo.findByOrderId(orderId);
        if (orderOpt.isEmpty())
            return null;

        Order order = orderOpt.get();

        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();

        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setFullname(order.getFullname());
        dto.setEmail(order.getEmail());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setUserId(order.getUser().getUserId());

        // Tính tổng tiền
        double totalAmount = order.getOrderItems().stream()
                .mapToDouble(item -> {
                    double discountedPrice = item.getOrderedProductPrice() * (1 - item.getDiscount() / 100);
                    return discountedPrice * item.getQuantity();
                })
                .sum();
        dto.setTotalAmount(totalAmount);

        // Mapping orderItems
        List<OrderItemDTO> items = order.getOrderItems().stream().map(item -> {
            OrderItemDTO i = new OrderItemDTO();
            i.setOrderItemId(item.getOrderItemId());
            i.setProductId(item.getProduct().getProductId());
            i.setProductName(item.getProduct().getProductName());
            i.setProductImage(item.getProduct().getImage());
            i.setQuantity(item.getQuantity());
            i.setOrderedProductPrice(item.getOrderedProductPrice());
            i.setDiscount(item.getDiscount());
            return i;
        }).collect(Collectors.toList());

        dto.setOrderItems(items);

        return dto;
    }

    private String mapOrderStatusToPaymentStatus(String orderStatus) {
        switch (orderStatus) {
            case "Đang xử lý":
                return "Chưa thanh toán";
            case "Đã thanh toán":
                return "Đã thanh toán";
            case "Đã hủy":
                return "Đã hủy";
            case "Chờ xác nhận":
                return "Chờ xác nhận";
            default:
                return "Không xác định"; // fallback nếu lạ
        }
    }

    @Override
    @Transactional
    public void updateOrderAndItems(Long orderId, OrderUpdateDTO orderUpdateDTO) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Cập nhật orderStatus cho đơn hàng
        if (orderUpdateDTO.getOrderStatus() != null) {
            order.setOrderStatus(orderUpdateDTO.getOrderStatus());

            // Map orderStatus sang paymentStatus
            String newOrderStatus = mapOrderStatusToPaymentStatus(orderUpdateDTO.getOrderStatus());

            order.setOrderStatus(newOrderStatus);

        }

        // Lưu lại
        orderRepo.save(order);
    }

}
