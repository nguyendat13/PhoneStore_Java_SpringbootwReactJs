package com.backend.backend_java.controller;

import com.backend.backend_java.payloads.FavoriteDTO;
import com.backend.backend_java.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/favorites") // Thêm "public" vào đường dẫn
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // Lấy danh sách sản phẩm yêu thích của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteDTO>> getFavoritesByUser(@PathVariable Long userId) {
        List<FavoriteDTO> favoriteList = favoriteService.getFavoritesByUser(userId);
        return new ResponseEntity<>(favoriteList, HttpStatus.OK);
    }

    // Thêm sản phẩm vào yêu thích
    @PostMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<FavoriteDTO> addFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        FavoriteDTO favoriteDTO = favoriteService.addFavorite(userId, productId);
        return new ResponseEntity<>(favoriteDTO, HttpStatus.CREATED);
    }

    // Xóa sản phẩm khỏi yêu thích
    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return new ResponseEntity<>("Sản phẩm đã bị xóa khỏi danh sách yêu thích", HttpStatus.OK);
    }
}
