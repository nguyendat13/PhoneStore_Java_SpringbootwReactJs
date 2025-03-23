package com.backend.backend_java.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend_java.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByProductNameLike(String keyword, Pageable pageDetails);

    Page<Product> findByProductNameContaining(String keyword, Pageable pageable);

    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByBrandBrandId(Long brandId, Pageable pageable);

    // @Query("SELECT p FROM Product p WHERE p.isDefault = true") // Thay đổi theo
    // logic của bạn
    // List<Product> findDefaultProducts();

    // Lấy ID lớn nhất hiện có
    @Query(value = "SELECT MAX(product_id) FROM products", nativeQuery = true)
    Long findMaxProductId();

    // Reset AUTO_INCREMENT theo ID lớn nhất hiện có
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE products AUTO_INCREMENT =1", nativeQuery = true)
    void resetAutoIncrement(Long nextId);

    @Query("SELECT p FROM Product p WHERE (p.category.categoryId = :categoryId OR p.brand.brandId = :brandId) AND p.productId <> :productId AND p.productName <> :productName")
    List<Product> findRelatedProducts(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("productName") String productName);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND p.productId <> :productId")
    List<Product> findProductsByCategory(@Param("categoryId") Long categoryId, @Param("productId") Long productId);

}
