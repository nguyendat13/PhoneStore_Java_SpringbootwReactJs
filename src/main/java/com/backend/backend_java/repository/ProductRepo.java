package com.backend.backend_java.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByProductNameLike(String keyword, Pageable pageDetails);

    Page<Product> findByProductNameContaining(String keyword, Pageable pageable);

    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);

}
