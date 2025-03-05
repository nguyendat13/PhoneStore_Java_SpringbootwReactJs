package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
}
