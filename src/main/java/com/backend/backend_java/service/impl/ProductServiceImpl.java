package com.backend.backend_java.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.Category;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
// import com.backend.backend_java.payloads.CartDTO;
import com.backend.backend_java.payloads.ProductDTO;
import com.backend.backend_java.payloads.ProductResponse;
// import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.CategoryRepo;
import com.backend.backend_java.repository.ProductRepo;
// import com.backend.backend_java.service.CartService;
import com.backend.backend_java.service.FileService;
import com.backend.backend_java.service.ProductService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    // @Autowired
    // private CartRepo cartRepo;

    // @Autowired
    // private CartService cartService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
        // Lấy danh mục, nếu không tìm thấy thì ném lỗi
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        // Kiểm tra xem sản phẩm đã tồn tại trong danh mục chưa
        if (isProductExistsInCategory(category, product)) {
            throw new APIException("Product already exists in this category!");
        }
        // Kiểm tra ID lớn nhất hiện tại
        Long maxId = productRepo.findMaxProductId();
        if (maxId == null) {
            maxId = 0L;
        }

        // Reset AUTO_INCREMENT
        productRepo.resetAutoIncrement(maxId + 1);
        // Thiết lập ảnh mặc định và gán danh mục
        product.setImage("default.png");
        product.setCategory(category);

        // Tính giá khuyến mãi
        double priceSale = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setPriceSale(priceSale);

        // Lưu sản phẩm vào database
        Product savedProduct = productRepo.save(product);

        // Cập nhật số lượng sản phẩm trong danh mục (+1)
        category.setCategoryQty(category.getCategoryQty() + 1);
        categoryRepo.save(category); // Lưu lại danh mục đã cập nhật

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    private boolean isProductExistsInCategory(Category category, Product product) {
        return category.getProducts().stream()
                .anyMatch(existingProduct -> existingProduct.getProductName().equalsIgnoreCase(product.getProductName())
                        &&
                        existingProduct.getDescription().equalsIgnoreCase(product.getDescription()));
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageDetails);
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId",
                        categoryId));
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        // Page<Product> pageProducts = productRepo.findAll(pageDetails);
        Page<Product> pageProducts = productRepo.findByCategoryCategoryId(categoryId, pageDetails);
        List<Product> products = pageProducts.getContent();
        if (products.size() == 0) {
            throw new APIException(category.getCategoryName() + " category doesn't contain any products !!!");
        }
        List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Long categoryId, Integer pageNumber, Integer pageSize,
            String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // Sử dụng "Containing" thay vì "Like" để tránh lỗi SQL
        Page<Product> pageProducts = productRepo.findByProductNameContaining(keyword, pageDetails);
        List<Product> products = pageProducts.getContent();

        // Nếu không có sản phẩm, trả về danh sách rỗng thay vì ném lỗi
        if (products.isEmpty()) {
            return new ProductResponse(Collections.emptyList(), pageNumber, pageSize, 0L, 0, true);
        }

        List<ProductDTO> productDTOs = products.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());

        return new ProductResponse(
                productDTOs,
                pageProducts.getNumber(),
                pageProducts.getSize(),
                pageProducts.getTotalElements(), // Lấy từ pageProducts thay vì tự tính
                pageProducts.getTotalPages(), // Lấy từ pageProducts thay vì tự tính
                pageProducts.isLast());
    }

    public ProductDTO updateProduct(Long productId, Product product) {
        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }
        // Giữ lại ảnh cũ nếu không có ảnh mới
        // if (product.getImage() != null) {
        // productFromDB.setImage(product.getImage());
        // }

        product.setImage(productFromDB.getImage());
        product.setProductId(productId);
        product.setCategory(productFromDB.getCategory());

        double priceSale = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setPriceSale(priceSale);

        Product saveProduct = productRepo.save(product);

        // List<Cart> carts = cartRepo.findCartsByProductID(productId);

        // List<CartDTO> cartDTOs = carts.stream().map(cart -> {
        // CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        // List<ProductDTO> products = cart.getCartItems().stream()
        // .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
        // .collect(Collectors.toList());

        // cartDTO.setProducts(products);

        // return cartDTO;

        // }).collect(Collectors.toList());
        // cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(),
        // productId));
        return modelMapper.map(saveProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }
        String fileName = fileService.uploadImage(path, image);
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepo.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Category category = product.getCategory();

        // Xóa sản phẩm khỏi database
        productRepo.delete(product);

        // Giảm số lượng sản phẩm trong danh mục (-1) nếu số lượng lớn hơn 0
        if (category.getCategoryQty() > 0) {
            category.setCategoryQty(category.getCategoryQty() - 1);
            categoryRepo.save(category); // Lưu lại danh mục đã cập nhật
        }

        return "Product with productId: " + productId + " deleted successfully !!!";
    }

    @Override
    public InputStream getProductImage(String fileName) throws FileNotFoundException {
        return fileService.getResource(path, fileName);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return modelMapper.map(product, ProductDTO.class);
        } else {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
    }

}
