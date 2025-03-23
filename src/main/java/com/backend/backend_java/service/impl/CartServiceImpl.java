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
import com.backend.backend_java.service.CartService;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Kiểm tra số lượng tồn kho
        if (product.getQuantity() < quantity) {
            throw new APIException("Chỉ còn " + product.getQuantity() + " sản phẩm trong kho.");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + quantity;
            if (newQuantity > product.getQuantity()) {
                throw new APIException("Không đủ hàng, chỉ còn " + product.getQuantity() + " sản phẩm.");
            }
            cartItem.setQuantity(newQuantity);
        } else {
            // Nếu chưa có sản phẩm trong giỏ, thêm mới
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(product.getPriceSale());

            cartItemRepo.save(cartItem);
        }

        // Cập nhật lại số lượng sản phẩm còn lại
        product.setQuantity(product.getQuantity() - quantity);
        productRepo.save(product);

        // Cập nhật tổng giá giỏ hàng
        double updatedTotalPrice = cart.getTotalPrice() + (product.getPriceSale() * quantity);
        cart.setTotalPrice(formatNumber(updatedTotalPrice));

        cartRepo.save(cart);

        // Chuyển đổi sang DTO và trả về kết quả
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setCartItems(convertToCartItemDTOList(cart));
        cartDTO.setEmail(cart.getUser().getEmail());

        return cartDTO;
    }

    // Chuyển đổi danh sách CartItem -> CartItemDTO
    private List<CartItemDTO> convertToCartItemDTOList(Cart cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem, CartItemDTO.class))
                .collect(Collectors.toList());
    }

    // Định dạng số để làm tròn về 2 chữ số thập phân
    private Double formatNumber(Double number) {
        if (number == null)
            return null;
        return Math.round(number * 100.0) / 100.0;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No cart exists");
        }

        return carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    cartDTO.setEmail(cart.getUser().getEmail()); // Gán email từ User

                    cartDTO.setCartItems(convertToCartItemDTOList(cart));
                    return cartDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setCartItems(convertToCartItemDTOList(cart));
        cartDTO.setEmail(cart.getUser().getEmail());
        return cartDTO;
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

        // First delete all cart items
        cartItemRepo.deleteCartItemsByCartId(cartId);

        // Then delete the cart
        cartRepo.delete(cart);
        cartRepo.flush(); // Force the delete operation

        return "Cart with ID " + cartId + " deleted successfully!!!";
    }
}