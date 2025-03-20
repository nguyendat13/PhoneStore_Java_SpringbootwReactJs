package com.backend.backend_java.service;

import com.backend.backend_java.payloads.BrandDTO;
import com.backend.backend_java.payloads.BrandResponse;

public interface BrandService {
    BrandDTO createBrand(BrandDTO brandDTO);

    BrandResponse getBrands(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    BrandDTO getBrandById(Long brandId);

    BrandDTO updateBrand(Long brandId, BrandDTO brandDTO);

    String deleteBrand(Long brandId);
}
