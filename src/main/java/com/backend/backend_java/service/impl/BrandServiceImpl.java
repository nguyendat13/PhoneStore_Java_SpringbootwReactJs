package com.backend.backend_java.service.impl;

import com.backend.backend_java.entity.Brand;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.BrandDTO;
import com.backend.backend_java.payloads.BrandResponse;
import com.backend.backend_java.repository.BrandRepo;
import com.backend.backend_java.repository.ProductRepo;
import com.backend.backend_java.service.BrandService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private BrandRepo brandRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BrandDTO createBrand(BrandDTO brandDTO) {
        if (brandRepo.findByBrandName(brandDTO.getBrandName()) != null) {
            throw new APIException("Brand with the name '" + brandDTO.getBrandName() + "' already exists!");
        }

        Brand brand = modelMapper.map(brandDTO, Brand.class);
        brand.setBrandQty(0L);
        brand = brandRepo.save(brand);
        return modelMapper.map(brand, BrandDTO.class);
    }

    @Override
    public BrandResponse getBrands(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Brand> brandPage = brandRepo.findAll(pageable);
        List<BrandDTO> brandDTOs = brandPage.getContent().stream()
                .map(brand -> {
                    BrandDTO dto = modelMapper.map(brand, BrandDTO.class);
                    // Đếm số sản phẩm theo brandId
                    Long qty = productRepo.countByBrand_BrandId(brand.getBrandId());
                    dto.setBrandQty(qty);
                    return dto;
                })
                .collect(Collectors.toList());

        return new BrandResponse(brandDTOs, brandPage.getNumber(), brandPage.getSize(),
                brandPage.getTotalElements(), brandPage.getTotalPages(), brandPage.isLast());
    }

    @Override
    public BrandDTO getBrandById(Long brandId) {
        Brand brand = brandRepo.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "brandId", brandId));
        return modelMapper.map(brand, BrandDTO.class);
    }

    @Override
    public BrandDTO updateBrand(Long brandId, BrandDTO brandDTO) {
        Brand brand = brandRepo.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "brandId", brandId));
        brand.setBrandName(brandDTO.getBrandName());
        // Gán mặc định 0 nếu brandQty bị null
        Long qty = brandDTO.getBrandQty() != null ? brandDTO.getBrandQty() : 0;
        brand.setBrandQty(qty);
        brand = brandRepo.save(brand);
        return modelMapper.map(brand, BrandDTO.class);
    }

    @Override
    public String deleteBrand(Long brandId) {
        Brand brand = brandRepo.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "brandId", brandId));
        if (!brand.getProducts().isEmpty()) {
            throw new APIException("Cannot delete brand with existing products.");
        }
        brandRepo.delete(brand);
        return "Brand with brandId: " + brandId + " deleted successfully!";
    }
}
