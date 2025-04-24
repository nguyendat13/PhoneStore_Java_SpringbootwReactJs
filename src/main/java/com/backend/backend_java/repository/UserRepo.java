package com.backend.backend_java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.backend_java.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.addresses a WHERE a.addressId = ?1")
    List<User> findByAddress(Long addressId);

    Optional<User> findByEmail(String email);

    // Lấy ID lớn nhất hiện có
    @Query(value = "SELECT MAX(user_id) FROM users", nativeQuery = true)
    Long findMaxUserId();

    // Reset AUTO_INCREMENT theo ID lớn nhất hiện có
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE users AUTO_INCREMENT =1", nativeQuery = true)
    void resetAutoIncrement(Long nextId);

    Page<User> findByRoles_RoleId(Integer roleId, Pageable pageable);

}
