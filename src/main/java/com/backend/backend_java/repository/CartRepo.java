package com.backend.backend_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.entity.Cart;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product p WHERE c.user.email = ?1 AND c.cartId = ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id=?1")
    List<Cart> findCartsByProductID(Long productId);

    
// Tìm giỏ hàng theo User ID
@Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
Cart findCartByUserId(Long userId);
    // Lấy ID lớn nhất hiện có
    @Query(value = "SELECT MAX(cart_id) FROM carts", nativeQuery = true)
    Long findMaxCartId();

    // Reset AUTO_INCREMENT theo ID lớn nhất hiện có
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE carts AUTO_INCREMENT =1", nativeQuery = true)
    void resetAutoIncrement(Long nextId);
    
}