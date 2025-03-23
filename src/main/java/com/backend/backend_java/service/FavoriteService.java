package com.backend.backend_java.service;

import com.backend.backend_java.payloads.FavoriteDTO;
import java.util.List;

public interface FavoriteService {
    FavoriteDTO addFavorite(Long userId, Long productId);

    void removeFavorite(Long userId, Long productId);

    List<FavoriteDTO> getFavoritesByUser(Long userId); // Thêm mới
}
