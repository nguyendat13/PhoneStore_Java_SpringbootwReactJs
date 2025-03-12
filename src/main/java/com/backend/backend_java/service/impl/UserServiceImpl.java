package com.backend.backend_java.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.backend_java.config.AppConstants;
import com.backend.backend_java.entity.Address;
import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.Favorite;
import com.backend.backend_java.entity.Role;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.AddressDTO;
import com.backend.backend_java.payloads.CartDTO;
import com.backend.backend_java.payloads.FavoriteDTO;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.payloads.ProductDTO;
import com.backend.backend_java.payloads.RoleDTO;
import com.backend.backend_java.payloads.UserDTO;
import com.backend.backend_java.payloads.UserReponse;
import com.backend.backend_java.repository.RoleRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.UserService;
import jakarta.transaction.Transactional;

// import com.backend.backend_java.entity.Address;
// import com.backend.backend_java.entity.Cart;
// import com.backend.backend_java.entity.Role;
// import com.backend.backend_java.service.CartService;
// import com.backend.backend_java.repository.AddressRepo;
// import com.backend.backend_java.repository.RoleRepo;
// import com.backend.backend_java.payloads.AddressDTO;
// import com.backend.backend_java.payloads.CartDTO;
@Transactional
@Service
public class UserServiceImpl implements UserService {

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private RoleRepo roleRepo;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private ModelMapper modelMapper;
        @Override
        public UserDTO registerUser(UserDTO userDTO) {
            try {
                Optional<User> existingUser = userRepo.findByEmail(userDTO.getEmail());
                if (existingUser.isPresent()) {
                    throw new APIException("⚠️ Email đã tồn tại: " + userDTO.getEmail());
                }
                
                User user = modelMapper.map(userDTO, User.class);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                
                // Handle roles
                Set<Role> roles = new HashSet<>();
                if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
                    // Use requested roles
                    for (Long roleId : userDTO.getRoleIds()) {
                        Role role = roleRepo.findById(roleId)
                            .orElseThrow(() -> new APIException("Role not found with id: " + roleId));
                        roles.add(role);
                    }
                } else {
                    // Add default USER role (role_id = 3)
                    Role userRole = roleRepo.findById(3L)  // Thay đổi ở đây
                        .orElseThrow(() -> new APIException("Default role not found"));
                    roles.add(userRole);
                }
                user.setRoles(roles);
                
                // Save user first
                User registeredUser = userRepo.save(user);
                
                // Then save user_roles relationship
                registeredUser.setRoles(roles);
                userRepo.save(registeredUser);
                
                // Return full user details
                return getUserById(registeredUser.getUserId());
                
            } catch (DataIntegrityViolationException e) {
                throw new APIException("User already exists with emailId: " + userDTO.getEmail());
            } catch (Exception e) {
                throw new APIException("Error occurred while registering user: " + e.getMessage());
            }
        }
       
        @Override
        public UserReponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<User> pageUsers = userRepo.findAll(pageDetails);
            
            // Map users giống như cách xử lý trong getUserById
            List<UserDTO> userDTOS = pageUsers.getContent().stream()
                    .map(user -> {
                        // Sử dụng lại cách mapping từ getUserById
                        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                        
                        // Set roleIds giống như trong getUserById
                        userDTO.setRoleIds(user.getRoles().stream()
                            .map(Role::getRoleId)
                            .collect(Collectors.toSet()));
        
                        // Set addresses giống như trong getUserById
                        userDTO.setAddresses(user.getAddresses().stream()
                            .map(address -> modelMapper.map(address, AddressDTO.class))
                            .collect(Collectors.toList()));
        
                        // Set carts giống như trong getUserById
                        userDTO.setCarts(user.getCarts().stream()
                            .map(cart -> modelMapper.map(cart, CartDTO.class))
                            .collect(Collectors.toList()));
        
                        // Set favorites giống như trong getUserById
                        userDTO.setFavorites(user.getFavorites().stream()
                            .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
                            .collect(Collectors.toList()));
        
                        // Set orders giống như trong getUserById
                        userDTO.setOrders(user.getOrders().stream()
                            .map(order -> modelMapper.map(order, OrderDTO.class))
                            .collect(Collectors.toList()));
        
                        return userDTO;
                    }).collect(Collectors.toList());
                    
            return new UserReponse(userDTOS, pageUsers.getNumber(), pageUsers.getSize(),
                    pageUsers.getTotalElements(), pageUsers.getTotalPages(), pageUsers.isLast());
        }
        @Override
public UserDTO getUserByEmail(String email) {
    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
            
    // Sử dụng lại logic từ getUserById
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    userDTO.setRoleIds(user.getRoles().stream()
            .map(Role::getRoleId)
            .collect(Collectors.toSet()));
            
    userDTO.setAddresses(user.getAddresses().stream()
            .map(address -> modelMapper.map(address, AddressDTO.class))
            .collect(Collectors.toList()));
            
    userDTO.setCarts(user.getCarts().stream()
            .map(cart -> modelMapper.map(cart, CartDTO.class))
            .collect(Collectors.toList()));
            
    userDTO.setFavorites(user.getFavorites().stream()
            .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
            .collect(Collectors.toList()));
            
    userDTO.setOrders(user.getOrders().stream()
            .map(order -> modelMapper.map(order, OrderDTO.class))
            .collect(Collectors.toList()));
            
    return userDTO;
}
        @Override
        public UserDTO getUserById(Long userId) {
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

                UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                userDTO.setRoleIds(user.getRoles().stream()
                                .map(Role::getRoleId)
                                .collect(Collectors.toSet())); // Lấy ID của Role

                userDTO.setAddresses(user.getAddresses().stream()
                                .map(address -> modelMapper.map(address, AddressDTO.class))
                                .collect(Collectors.toList()));

                userDTO.setCarts(user.getCarts().stream()
                                .map(cart -> modelMapper.map(cart, CartDTO.class))
                                .collect(Collectors.toList()));

                userDTO.setFavorites(user.getFavorites().stream()
                                .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
                                .collect(Collectors.toList()));

                userDTO.setOrders(user.getOrders().stream()
                                .map(order -> modelMapper.map(order, OrderDTO.class))
                                .collect(Collectors.toList()));

                return userDTO;
        }

        @Override
        public UserDTO updateUser(Long userId, UserDTO userDTO) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
            
            user.setFullname(userDTO.getFullname());
            user.setPhone(userDTO.getPhone());
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getUsername());
            user.setGender(userDTO.getGender());
            
            // Cập nhật password nếu có
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            
            // Cập nhật roles nếu có
            if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
                Set<Role> roles = userDTO.getRoleIds().stream()
                    .map(roleId -> roleRepo.findById(roleId)
                        .orElseThrow(() -> new APIException("Role not found with id: " + roleId)))
                    .collect(Collectors.toSet());
                user.setRoles(roles);
            }
            
            User updatedUser = userRepo.save(user);
            return getUserById(updatedUser.getUserId()); // Sử dụng getUserById để đảm bảo response đầy đủ
        }

        @Override
        public String deleteUser(Long userId) {
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
                userRepo.delete(user);
                return "User with userId " + userId + " deleted successfully!!!";
        }
}
