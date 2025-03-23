package com.backend.backend_java.service.impl;

import com.backend.backend_java.entity.Favorite;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.FavoriteDTO;
import com.backend.backend_java.repository.FavoriteRepo;
import com.backend.backend_java.repository.ProductRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.FavoriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepo favoriteRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FavoriteDTO addFavorite(Long userId, Long productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách yêu thích chưa
        boolean exists = favoriteRepo.findByUser(user).stream()
                .anyMatch(fav -> fav.getProduct().getProductId().equals(productId));

        if (exists) {
            throw new RuntimeException("Sản phẩm đã có trong danh sách yêu thích!");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favorite.setCreatedAt(LocalDateTime.now());

        Favorite savedFavorite = favoriteRepo.save(favorite);

        return modelMapper.map(savedFavorite, FavoriteDTO.class);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Favorite favorite = favoriteRepo.findByUser(user).stream()
                .filter(fav -> fav.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "productId", productId));

        favoriteRepo.delete(favorite);
    }

    @Override
    public List<FavoriteDTO> getFavoritesByUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        List<Favorite> favorites = favoriteRepo.findByUser(user);

        return favorites.stream()
                .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
                .collect(Collectors.toList());
    }
}
