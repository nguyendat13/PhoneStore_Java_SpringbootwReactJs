package com.backend.backend_java.repository;

import com.backend.backend_java.entity.Favorite;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface FavoriteRepo extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<Favorite> findByUser(User user);

}
