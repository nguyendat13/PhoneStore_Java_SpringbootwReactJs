package com.backend.backend_java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.entity.Address;

import jakarta.transaction.Transactional;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
    // Tìm địa chỉ dựa trên các thông tin chi tiết
    @Query("SELECT a FROM Address a WHERE a.street = ?1 AND a.buildingName = ?2 AND a.city = ?3 AND a.state = ?4 AND a.country = ?5 AND a.pincode = ?6")
    Optional<Address> findByAddressDetails(String street, String buildingName, String city, String state, String country, String pincode);
  
  
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_address WHERE user_id = ?1", nativeQuery = true)
    void deleteAddressesByUserId(Long userId);

    
 // Lấy ID lớn nhất hiện có
 @Query(value = "SELECT MAX(address_id) FROM addresses", nativeQuery = true)
 Long findMaxAddressId();

 // Reset AUTO_INCREMENT theo ID lớn nhất hiện có
 @Modifying
 @Transactional
 @Query(value = "ALTER TABLE addresses AUTO_INCREMENT =1", nativeQuery = true)
 void resetAutoIncrement(Long nextId);
} 