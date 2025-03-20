package com.backend.backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.payloads.BrandDTO;
import com.backend.backend_java.payloads.BrandResponse;
import com.backend.backend_java.service.BrandService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@CrossOrigin(origins = "*")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/admin/brands")
    public ResponseEntity<BrandDTO> createBrand(@Valid @RequestBody BrandDTO brandDTO) {
        BrandDTO savedBrandDTO = brandService.createBrand(brandDTO);
        return new ResponseEntity<>(savedBrandDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/brands")
    public ResponseEntity<BrandResponse> getBrands(
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "brandId") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {

        BrandResponse brandResponse = brandService.getBrands(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(brandResponse, HttpStatus.OK);
    }

    @GetMapping("/public/brands/{brandId}")
    public ResponseEntity<BrandDTO> getOneBrand(@PathVariable Long brandId) {
        BrandDTO brandDTO = brandService.getBrandById(brandId);
        return new ResponseEntity<>(brandDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/brands/{brandId}")
    public ResponseEntity<BrandDTO> updateBrand(@RequestBody BrandDTO brandDTO, @PathVariable Long brandId) {
        BrandDTO updatedBrandDTO = brandService.updateBrand(brandId, brandDTO);
        return new ResponseEntity<>(updatedBrandDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/brands/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId) {
        String status = brandService.deleteBrand(brandId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
