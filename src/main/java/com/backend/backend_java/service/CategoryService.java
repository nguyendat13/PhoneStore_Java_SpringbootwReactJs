package com.backend.backend_java.service;

import com.backend.backend_java.entity.Category;
import com.backend.backend_java.payloads.CategoryDTO;
import com.backend.backend_java.payloads.CategoryResponse;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    String deleteCategory(Long categoryId);

    
}
