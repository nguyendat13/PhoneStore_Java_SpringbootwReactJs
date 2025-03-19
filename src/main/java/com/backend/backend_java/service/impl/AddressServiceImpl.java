package com.backend.backend_java.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend_java.entity.Address;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.AddressDTO;
import com.backend.backend_java.repository.AddressRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.AddressService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        // Kiểm tra ID lớn nhất hiện tại
        Long maxId = addressRepo.findMaxAddressId();
        if (maxId == null) {
            maxId = 0L;
        }

        // Reset AUTO_INCREMENT     
        Address address = modelMapper.map(addressDTO, Address.class);
        Address savedAddress = addressRepo.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepo.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        address.setStreet(addressDTO.getStreet());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setPincode(addressDTO.getPincode());

        Address updatedAddress = addressRepo.save(address);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        
        // Remove this address from all users before deleting
        address.getUsers().forEach(user -> user.getAddresses().remove(address));
        
        addressRepo.delete(address);
        return "Address deleted successfully";
    }

    @Override
    public AddressDTO addAddressToUser(Long userId, AddressDTO addressDTO) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Address address = modelMapper.map(addressDTO, Address.class);
        address = addressRepo.save(address);

        // Add address to user's addresses
        user.getAddresses().add(address);
        // Add user to address's users
        address.getUsers().add(user);

        userRepo.save(user);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public String removeAddressFromUser(Long userId, Long addressId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        if (!user.getAddresses().contains(address)) {
            throw new APIException("Address is not associated with this user");
        }

        user.getAddresses().remove(address);
        address.getUsers().remove(user);

        userRepo.save(user);
        return "Address removed from user successfully";
    }

    @Override
    public List<AddressDTO> getAddressesByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return user.getAddresses().stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    // Thêm phương thức mới để xử lý địa chỉ khi đăng ký user
    public Address createOrGetAddress(AddressDTO addressDTO) {
        // Kiểm tra xem địa chỉ đã tồn tại chưa
        Optional<Address> existingAddress = addressRepo.findByAddressDetails(
            addressDTO.getStreet(),
            addressDTO.getBuildingName(),
            addressDTO.getCity(),
            addressDTO.getState(),
            addressDTO.getCountry(),
            addressDTO.getPincode()
        );

        if (existingAddress.isPresent()) {
            return existingAddress.get();
        }
             // Kiểm tra ID lớn nhất hiện tại
             Long maxId = addressRepo.findMaxAddressId();
             if (maxId == null) {
                 maxId = 0L;
             }

             // Reset AUTO_INCREMENT
             addressRepo.resetAutoIncrement(maxId + 1);
        // Nếu địa chỉ chưa tồn tại, tạo mới
        Address address = modelMapper.map(addressDTO, Address.class);
        return addressRepo.save(address);
    }
} 