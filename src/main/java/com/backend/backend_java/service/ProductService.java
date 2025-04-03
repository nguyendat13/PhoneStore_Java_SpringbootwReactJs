package com.backend.backend_java.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend_java.entity.Product;
import com.backend.backend_java.payloads.ProductDTO;
import com.backend.backend_java.payloads.ProductResponse;

public interface ProductService {
        ProductDTO addProduct(Long brandId, Long categoryId, Product product);

        ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

        ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                        String sortOrder);

        ProductResponse searchByBrand(Long brandId, Integer pageNumber, Integer pageSize, String sortBy,
                        String sortOrder);

        ProductDTO updateProduct(Long categoryId, Long brandId, Long productId, Product product);

        ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

        public InputStream getProductImage(String fileName) throws FileNotFoundException;

        ProductResponse searchProductByKeyword(String keyword, Long categoryId, Integer pageNumber, Integer pageSize,
                        String sortBy,
                        String sortOrder);

        String deleteProduct(Long productId);

        ProductDTO getProductById(Long productId);

        List<ProductDTO> getRelatedProducts(Long productId);

        List<ProductDTO> getNewProducts();

}
