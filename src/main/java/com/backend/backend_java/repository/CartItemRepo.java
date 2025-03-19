package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.CartItem;
import com.backend.backend_java.entity.Product;
import java.util.List;

import jakarta.transaction.Transactional;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    
    // Find a Product by its ID through CartItem
    @Query("SELECT ci.product FROM CartItem ci WHERE ci.product.id = ?1")
    Product findProductById(Long productId);

    // Find CartItems by Cart ID and Product ID
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);

    // Delete CartItem by Cart ID and Product ID
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);

    // Find CartItems for a given Cart (for example, by Cart ID)
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1")
    List<CartItem> findCartItemsByCartId(Long cartId);

    // Find Carts associated with a specific Product (this method is commented in your code)
    @Query("SELECT ci.cart FROM CartItem ci WHERE ci.product.id = ?1")
    List<Cart> findCartsByProductId(Long productId);

    // Optional: Query to find Cart by User Email and Cart ID (requires proper User-Cart relation)
    @Query("SELECT ci.cart FROM CartItem ci WHERE ci.cart.user.email = ?1 AND ci.cart.id = ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteCartItemsByCartId(@Param("cartId") Long cartId);

    // Lấy ID lớn nhất hiện có
    @Query(value = "SELECT MAX(cart_item_id) FROM cart_items", nativeQuery = true)
    Long findMaxCartItemId();

    // Reset AUTO_INCREMENT theo ID lớn nhất hiện có
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE cart_items AUTO_INCREMENT =1", nativeQuery = true)
    void resetAutoIncrement(Long nextId);
    

}