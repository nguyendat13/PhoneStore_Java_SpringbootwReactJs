package com.backend.backend_java.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.backend.backend_java.payloads.CartDTO;
import com.backend.backend_java.payloads.request.CartRequest;
import com.backend.backend_java.service.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    // // Thêm sản phẩm vào giỏ hàng
    // @PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    // public ResponseEntity<CartDTO> addProductToCart(
    // @PathVariable Long cartId,
    // @PathVariable Long productId,
    // @PathVariable Integer quantity) {
    // CartDTO cartDTO = cartService.addProductToCart(cartId, productId, quantity);
    // return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    // }

    // Lấy tất cả giỏ hàng (dành cho admin)
    @GetMapping("/admin/carts")
    public ResponseEntity<Map<String, Object>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        Map<String, Object> response = new HashMap<>();
        response.put("content", cartDTOs);
        response.put("totalElements", cartDTOs.size());
        response.put("totalPages", 1); // Chưa có phân trang
        return ResponseEntity.ok(response);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateCartProduct(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    // Xóa một sản phẩm khỏi giỏ hàng
    @DeleteMapping("/public/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.ok(status);
    }

    // Xóa toàn bộ giỏ hàng
    @DeleteMapping("/public/carts/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId) {
        String status = cartService.deleteCart(cartId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/public/cart/{cartId}")
    public ResponseEntity<CartDTO> getCartByCartId(@PathVariable Long cartId) {
        CartDTO cart = cartService.getCartByCartId(cartId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/public/cart/add")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest cartRequest) {
        cartService.addToCart(cartRequest);
        return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ hàng");
    }

}
