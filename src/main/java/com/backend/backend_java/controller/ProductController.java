package com.backend.backend_java.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend_java.config.AppConstants;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.payloads.CategoryResponse;
import com.backend.backend_java.payloads.ProductDTO;
import com.backend.backend_java.payloads.ProductResponse;
import com.backend.backend_java.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product/brands/{brandId}")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable Long categoryId,
            @PathVariable Long brandId, // Nhận thêm brandId từ URL
            @Valid @RequestBody Product product) {

        try {
            ProductDTO savedProduct = productService.addProduct(brandId, categoryId, product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (APIException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
        @RequestParam(name = "selectedCategory", required = false) Long selectedCategory,
        @RequestParam(name = "selectedBrand", required = false) Long selectedBrand
    ) {
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;
    
        ProductResponse productResponse = productService.getAllProducts(
            pageNumber, pageSize, sortBy, sortOrder, selectedCategory, selectedBrand
        );
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    
    
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        // Kiểm tra nếu pageNumber nhỏ hơn hoặc bằng 0 thì chuyển thành 0
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;

        ProductResponse productResponse = productService.searchByCategory(
                categoryId,
                pageNumber,
                pageSize,
                "id".equals(sortBy) ? "productId" : sortBy,
                sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/brands/{brandId}/products")
    public ResponseEntity<ProductResponse> getProductsByBrand(
            @PathVariable Long brandId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        // Kiểm tra nếu pageNumber nhỏ hơn hoặc bằng 0 thì chuyển thành 0
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;

        ProductResponse productResponse = productService.searchByBrand(
                brandId,
                pageNumber,
                pageSize,
                "id".equals(sortBy) ? "productId" : sortBy,
                sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            @RequestParam(name = "categoryId", defaultValue = "0", required = false) Long categoryId) {

        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;
        ProductResponse productResponse = productService.searchProductByKeyword(
                keyword,
                categoryId,
                pageNumber,
                pageSize,
                "id".equals(sortBy) ? "productId" : sortBy,
                sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/image/{fileName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String fileName) throws FileNotFoundException {
        InputStream imageStream = productService.getProductImage(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("inline", fileName);
        return new ResponseEntity<>(new InputStreamResource(imageStream), headers, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}/product/{productId}/brands/{brandId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long categoryId,
            @PathVariable Long productId,
            @PathVariable Long brandId,
            @RequestBody Product product) {
        ProductDTO updatedProduct = productService.updateProduct(productId, brandId, categoryId, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new APIException("File image is missing!");
        }
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/public/products/{productId}/related")
    public ResponseEntity<List<ProductDTO>> getRelatedProducts(@PathVariable Long productId) {
        List<ProductDTO> relatedProducts = productService.getRelatedProducts(productId);
        return ResponseEntity.ok(relatedProducts);
    }

    // Sản phẩm mới
    @GetMapping("/public/products/new")
    public ResponseEntity<ProductResponse> getNewProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "createdDate", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder) {

        // Giảm pageNumber đi 1 nếu pageNumber lớn hơn 0 (do PageRequest bắt đầu từ 0)
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;

        // Gọi service để lấy sản phẩm mới với tham số phân trang và sắp xếp
        ProductResponse productResponse = productService.getLatestProducts(pageNumber, pageSize, sortBy, sortOrder);

        // Trả về dữ liệu với ResponseEntity
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Sản phẩm khuyến mãi
    @GetMapping("/public/products/sale")
    public ResponseEntity<ProductResponse> getSaleProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "createdDate", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder) {

        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;
        ProductResponse productResponse = productService.getSaleProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Sản phấm bán chạy
    @GetMapping("/public/products/best-sellers")
    public ResponseEntity<ProductResponse> getBestSellingProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {

        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;
        ProductResponse response = productService.getBestSellingProducts(pageNumber, pageSize, "quantity", "asc");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
