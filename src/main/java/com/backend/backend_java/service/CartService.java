package com.backend.backend_java.service;

import java.util.List;

import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.payloads.CartDTO;
import com.backend.backend_java.payloads.CartItemDTO;
import com.backend.backend_java.payloads.request.CartRequest;

public interface CartService {
    // CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String email, Long cartId);

    CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);

    void updateProductInCarts(Long cartId, Long productId);

    String deleteProductFromCart(Long cartId, Long productId);

    String deleteCart(Long cartId);

    CartDTO convertCartToDTO(Cart cart);

    CartDTO getCartByCartId(Long cartId);

    List<CartDTO> getCartsByUserRole(String roleName);

    CartItemDTO addToCart(CartRequest request);

}