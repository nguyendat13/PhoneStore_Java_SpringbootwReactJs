package com.backend.backend_java.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    // @Override
    // public CategoryDTO createCategory(Category category) {

    // Category savedCategory =
    // categoryRepo.findByCategoryName(category.getCategoryName());

    // if (savedCategory != null) {

    // throw new APIException("Category with the name'" + category.getCategoryName()
    // + " 'already exists !!!");
    // }
    // savedCategory = categoryRepo.save(category);
    // return modelMapper.map(savedCategory, CategoryDTO.class);
    // }
    @Override
    public CategoryDTO createCategory(Category category) {
        // Kiểm tra nếu danh mục đã tồn tại
        Optional<Category> existingCategory = Optional
                .ofNullable(categoryRepo.findByCategoryName(category.getCategoryName()));

        if (existingCategory.isPresent()) {
            throw new APIException("Category with the name '" +
                    category.getCategoryName() + "' already exists !!!");
        }

        // Lưu danh mục mới
        Category savedCategory = categoryRepo.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // Xác định hướng sắp xếp (ascending hoặc descending)
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tạo đối tượng Pageable để phân trang và sắp xếp
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // Truy vấn cơ sở dữ liệu với phân trang và sắp xếp
        Page<Category> pageCategories = categoryRepo.findAll(pageDetails);

        // Lấy danh sách các category từ page
        List<Category> categories = pageCategories.getContent();

        // Chuyển các category sang DTO
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        // Tạo CategoryResponse và gán các thông tin phân trang
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalElements(pageCategories.getTotalElements());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return modelMapper.map(category, CategoryDTO.class);
        } else {
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        }
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, Category category) {

        Category savedCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepo.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = category.getProducts();

        products.forEach(product -> {
            productService.deleteProduct(product.getProductId());
        });

        categoryRepo.delete(category);
        return "Category with categoryId: " + categoryId + " deleted successfully !!!";
    }

}
