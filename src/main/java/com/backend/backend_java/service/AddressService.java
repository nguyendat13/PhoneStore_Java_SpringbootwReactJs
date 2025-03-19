package com.backend.backend_java.service;

import java.util.List;

import com.backend.backend_java.entity.Address;
import com.backend.backend_java.payloads.AddressDTO;

public interface AddressService {
    // Create or get existing address
    Address createOrGetAddress(AddressDTO addressDTO);

    // Create
    AddressDTO createAddress(AddressDTO addressDTO);

    // Read
    AddressDTO getAddressById(Long addressId);
    List<AddressDTO> getAllAddresses();

    // Update
    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    // Delete
    String deleteAddress(Long addressId);

    // Add address to user
    AddressDTO addAddressToUser(Long userId, AddressDTO addressDTO);

    // Remove address from user
    String removeAddressFromUser(Long userId, Long addressId);

    // Get all addresses for a user
    List<AddressDTO> getAddressesByUserId(Long userId);
} 