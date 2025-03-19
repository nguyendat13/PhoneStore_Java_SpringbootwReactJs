package com.backend.backend_java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.payloads.AddressDTO;
import com.backend.backend_java.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // CREATE
    @PostMapping("/public/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(addressService.createAddress(addressDTO), HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/public/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @GetMapping("/public/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    // UPDATE
    @PutMapping("/public/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, addressDTO));
    }

    // DELETE
    @DeleteMapping("/public/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.deleteAddress(addressId));
    }

    // User-Address Relationship Management
    @PostMapping("/public/users/{userId}/addresses")
    public ResponseEntity<AddressDTO> addAddressToUser(
            @PathVariable Long userId,
            @Valid @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(addressService.addAddressToUser(userId, addressDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/public/users/{userId}/addresses/{addressId}")
    public ResponseEntity<String> removeAddressFromUser(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.removeAddressFromUser(userId, addressId));
    }

    @GetMapping("/public/users/{userId}/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }
} 