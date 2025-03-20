package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.entity.Brand;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long> {
    Brand findByBrandName(String brandName);

    @Query(value = "SELECT MAX(brand_id) FROM brands", nativeQuery = true)
    Long findMaxBrandId();
}
