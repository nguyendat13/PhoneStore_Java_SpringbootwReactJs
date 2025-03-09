package com.backend.backend_java.service.impl;

import com.backend.backend_java.entity.Category;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.CategoryDTO;
import com.backend.backend_java.payloads.CategoryResponse;
import com.backend.backend_java.repository.CategoryRepo;
import com.backend.backend_java.service.CategoryService;
import com.backend.backend_java.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepo.findByCategoryName(categoryDTO.getCategoryName()) != null) {
            throw new APIException("Category with the name '" + categoryDTO.getCategoryName() + "' already exists !!!");
        }
        // Kiểm tra ID lớn nhất hiện tại
        Long maxId = categoryRepo.findMaxCategoryId();
        if (maxId == null) {
            maxId = 0L;
        }

        // Reset AUTO_INCREMENT
        categoryRepo.resetAutoIncrement(maxId + 1);

        Category category = modelMapper.map(categoryDTO, Category.class); // Chuyển DTO sang Entity
        category.setCategoryQty(0); // Mặc định số lượng sản phẩm là 0 khi tạo danh mục mới
        category = categoryRepo.save(category); // Lưu vào database
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        List<CategoryDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        return new CategoryResponse(categoryDTOs, categoryPage.getNumber(), categoryPage.getSize(),
                categoryPage.getTotalElements(), categoryPage.getTotalPages(), categoryPage.isLast());
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryQty(categoryDTO.getCategoryQty());
        category = categoryRepo.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        if (!category.getProducts().isEmpty()) {
            throw new APIException("Cannot delete category with existing products.");
        }
        List<Product> products = category.getProducts();

        products.forEach(product -> {
            productService.deleteProduct(product.getProductId());
        });

        categoryRepo.delete(category);
        return "Category with categoryId: " + categoryId + " deleted successfully !!!";
    }
}
