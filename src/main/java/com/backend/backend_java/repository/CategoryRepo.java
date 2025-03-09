package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend_java.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);

    // Lấy ID lớn nhất hiện có
    @Query(value = "SELECT MAX(category_id) FROM categories", nativeQuery = true)
    Long findMaxCategoryId();

    // Reset AUTO_INCREMENT theo ID lớn nhất hiện có
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE categories AUTO_INCREMENT = ?1", nativeQuery = true)
    void resetAutoIncrement(Long nextId);
}
