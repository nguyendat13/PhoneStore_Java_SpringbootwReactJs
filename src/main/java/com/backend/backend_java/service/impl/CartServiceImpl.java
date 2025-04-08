package com.backend.backend_java.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.CartItem;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.CartDTO;
import com.backend.backend_java.payloads.CartItemDTO;
import com.backend.backend_java.payloads.ProductDTO;
import com.backend.backend_java.repository.CartItemRepo;
import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.ProductRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.CartService;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Thêm sản phẩm vào giỏ hàng
     * 
     * @param cartId    ID giỏ hàng
     * @param productId ID sản phẩm
     * @param quantity  Số lượng thêm
     * @return CartDTO - DTO giỏ hàng cập nhật
     * @throws ResourceNotFoundException nếu không tìm thấy giỏ hàng/sản phẩm
     * @throws APIException              nếu số lượng vượt quá tồn kho
     */
    @Override
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
        // 1. Validate input
        if (quantity <= 0) {
            throw new APIException("Số lượng phải lớn hơn 0");
        }
        // Kiểm tra ID lớn nhất hiện tại
        Long maxId1 = cartItemRepo.findMaxCartItemId();
        if (maxId1 == null) {
            maxId1 = 0L;
        }
        // Reset AUTO_INCREMENT
        cartItemRepo.resetAutoIncrement(maxId1 + 1);

        // 2. Tìm giỏ hàng và sản phẩm
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // 3. Kiểm tra số lượng tồn kho
        if (product.getQuantity() < quantity) {
            throw new APIException(String.format(
                    "Sản phẩm '%s' chỉ còn %d trong kho",
                    product.getProductName(),
                    product.getQuantity()));
        }

        // 4. Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // 4a. Nếu đã có, cập nhật số lượng
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > product.getQuantity()) {
                throw new APIException(String.format(
                        "Tổng số lượng %d vượt quá số lượng tồn kho (%d)",
                        newQuantity,
                        product.getQuantity()));
            }
            existingItem.setQuantity(newQuantity);
        } else {
            // 4b. Nếu chưa có, thêm mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setProductPrice(product.getPriceSale());
            newItem.setDiscount(product.getDiscount());

            cart.getCartItems().add(newItem);

            // Nhưng thiếu:
            cartItemRepo.save(newItem); // ⚠️ Cần thêm dòng này để đảm bảo lưu
        }

        // 5. Cập nhật tồn kho và tổng giá
        product.setQuantity(product.getQuantity() - quantity);
        updateCartTotalPrice(cart);

        // 6. Lưu các thay đổi
        productRepo.save(product);
        cartRepo.save(cart);

        // 7. Convert sang DTO và trả về
        return convertCartToDTO(cart);
    }

    private void updateCartTotalPrice(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(Math.round(total * 100.0) / 100.0); // Làm tròn 2 chữ số
    }

    public CartDTO convertCartToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setTotalPrice(cart.getTotalPrice());

        if (cart.getUser() != null) {
            dto.setEmail(cart.getUser().getEmail());
        }

        // Kiểm tra cartItems và chuyển đổi nếu có
        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            // Convert cart items
            dto.setCartItems(convertToCartItemDTOList(cart));
        } else {
            // Nếu không có cartItems, bạn có thể gán danh sách rỗng hoặc xử lý đặc biệt
            dto.setCartItems(Collections.emptyList());
        }

        return dto;
    }

    private CartItemDTO convertCartItemToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(item.getCartItemId());
        dto.setQuantity(item.getQuantity());

        // Giữ nguyên kiểu double
        dto.setProductPrice(item.getProductPrice());
        dto.setDiscount(item.getDiscount());

        if (item.getCart() != null) {
            dto.setCartId(item.getCart().getCartId());// them cart id
        }
        // Product info
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getProductId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setProductImage(item.getProduct().getImage());
        }

        return dto;
    }

    /**
     * Chuyển đổi danh sách CartItem của giỏ hàng sang DTO
     * 
     * @param cart Entity giỏ hàng
     * @return Danh sách CartItemDTO
     */
    private List<CartItemDTO> convertToCartItemDTOList(Cart cart) {
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return Collections.emptyList();
        }
        return cart.getCartItems().stream()
                .map(this::convertCartItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        return carts.stream()
                .map(this::convertCartToDTO) // <- dùng hàm tự ánh xạ
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        return convertCartToDTO(cart);
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getPriceSale());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepo.save(cartItem);
    }

    @Override
    public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);
        cartItem.setProductPrice(product.getPriceSale());
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));

        cartItemRepo.save(cartItem);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setCartItems(convertToCartItemDTOList(cart));
        cartDTO.setEmail(cart.getUser().getEmail());
        cartDTO.setTotalPrice(formatNumber(cart.getTotalPrice())); // Format total price

        return cartDTO;
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());

        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart!!!";
    }

    @Override
    @Transactional
    public String deleteCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        // Kiểm tra và cập nhật User
        if (cart.getUser() != null) {
            cart.getUser().setCart(null); // Đặt Cart của User thành null trước khi xóa
            userRepo.save(cart.getUser());
        }

        // Xóa các CartItem và Cart
        cartItemRepo.deleteCartItemsByCartId(cartId);
        cartRepo.delete(cart);
        cartRepo.flush(); // Force the delete operation

        return "Cart with ID " + cartId + " deleted successfully!!!";
    }

    // Định dạng số để làm tròn về 2 chữ số thập phân
    private Double formatNumber(Double number) {
        if (number == null)
            return null;
        return Math.round(number * 100.0) / 100.0;
    }

}