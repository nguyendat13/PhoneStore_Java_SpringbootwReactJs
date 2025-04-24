package com.backend.backend_java.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend_java.config.AppConstants;
import com.backend.backend_java.entity.Address;
import com.backend.backend_java.entity.Cart;
import com.backend.backend_java.entity.CartItem;
import com.backend.backend_java.entity.Favorite;
import com.backend.backend_java.entity.Product;
import com.backend.backend_java.entity.Role;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.APIException;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.AddressDTO;
import com.backend.backend_java.payloads.CartDTO;
import com.backend.backend_java.payloads.CartItemDTO;
import com.backend.backend_java.payloads.FavoriteDTO;
import com.backend.backend_java.payloads.OrderDTO;
import com.backend.backend_java.payloads.ProductDTO;
import com.backend.backend_java.payloads.RoleDTO;
import com.backend.backend_java.payloads.UserDTO;
import com.backend.backend_java.payloads.UserReponse;
import com.backend.backend_java.repository.AddressRepo;
import com.backend.backend_java.repository.CartItemRepo;
import com.backend.backend_java.repository.CartRepo;
import com.backend.backend_java.repository.ProductRepo;
import com.backend.backend_java.repository.RoleRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.UserService;
import com.backend.backend_java.service.AddressService;
import com.backend.backend_java.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
    private CartService cartService;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressService addressService;

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        try {
            // Check if user exists
            if (userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new APIException("Email đã tồn tại: " + userDTO.getEmail());
            }
            // if (userRepo.existsByUsername(userDTO.getUsername())) {
            // throw new ResourceAlreadyExistsException("Username đã tồn tại!");
            // }
            // Kiểm tra ID lớn nhất hiện tại
            Long maxId = userRepo.findMaxUserId();
            if (maxId == null) {
                maxId = 0L;
            }
            // Reset AUTO_INCREMENT
            userRepo.resetAutoIncrement(maxId + 1);

            // Kiểm tra ID lớn nhất hiện tại
            Long maxId1 = cartRepo.findMaxCartId();
            if (maxId1 == null) {
                maxId1 = 0L;
            }
            // Reset AUTO_INCREMENT
            cartRepo.resetAutoIncrement(maxId1 + 1);

            // Map DTO to entity
            User user = modelMapper.map(userDTO, User.class);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            // Set roles
            Set<Role> roles = new HashSet<>();
            userDTO.getRoleIds().forEach(roleId -> {
                Role role = roleRepo.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));
                roles.add(role);
            });
            user.setRoles(roles);

            // Set addresses
            if (userDTO.getAddresses() != null) {
                user.setAddresses(userDTO.getAddresses().stream()
                        .map(addressDTO -> {
                            // Use addressService to create or get address
                            Address address = addressService.createOrGetAddress(addressDTO);
                            address.setUsers(List.of(user));
                            return address;
                        })
                        .collect(Collectors.toList()));
            }

            Cart cart = cartRepo.findCartByUserId(user.getUserId()); // Tìm cart theo user ID
            if (cart == null) {
                cart = new Cart(); // Tạo mới nếu chưa có
            }
            cart.setUser(user);
            cart.setTotalPrice(userDTO.getCart() != null ? userDTO.getCart().getTotalPrice() : 0.0);
            cart.setEmail(userDTO.getEmail()); // Gán email vào cart khi tạo mới
            cart = cartRepo.save(cart); // Lưu lại trước khi gán vào user
            user.setCart(cart);

            // Save user (this will also save the cart due to cascade)
            User registeredUser = userRepo.save(user);

            // Map to DTO and ensure cart email matches user email
            UserDTO responseDTO = modelMapper.map(registeredUser, UserDTO.class);
            if (responseDTO.getCart() != null) {
                responseDTO.getCart().setEmail(registeredUser.getEmail());
                responseDTO.getCart().setCartId(1L); // Ensure cart ID is 1 in response
            }

            // Set roleIds in response
            responseDTO.setRoleIds(registeredUser.getRoles().stream()
                    .map(Role::getRoleId)
                    .collect(Collectors.toSet()));

            // Set addresses in response
            responseDTO.setAddresses(registeredUser.getAddresses().stream()
                    .map(address -> modelMapper.map(address, AddressDTO.class))
                    .collect(Collectors.toList()));

            // Set favorites in response
            responseDTO.setFavorites(registeredUser.getFavorites().stream()
                    .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
                    .collect(Collectors.toList()));

            // Set orders in response
            responseDTO.setOrders(registeredUser.getOrders().stream()
                    .map(order -> modelMapper.map(order, OrderDTO.class))
                    .collect(Collectors.toList()));

            return responseDTO;
        } catch (DataIntegrityViolationException e) {
            throw new APIException("Registration error: User already exists with email: " + userDTO.getEmail());
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            throw new APIException("Unknown error during user registration: " + e.getMessage());
        }
    }

    // @Override
    // @Transactional(readOnly = true)
    // public UserReponse getAllUsers(Integer pageNumber, Integer pageSize, String
    // sortBy, String sortOrder) {
    // Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
    // ? Sort.by(sortBy).ascending()
    // : Sort.by(sortBy).descending();

    // Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    // Page<User> pageUsers = userRepo.findAll(pageDetails);

    // List<UserDTO> userDTOS = pageUsers.getContent().stream()
    // .map(this::convertToUserDTO) // dùng hàm mới
    // .collect(Collectors.toList());

    // return new UserReponse(
    // userDTOS,
    // pageUsers.getNumber(),
    // pageUsers.getSize(),
    // pageUsers.getTotalElements(),
    // pageUsers.getTotalPages(),
    // pageUsers.isLast());
    // }

    @Override
    @Transactional(readOnly = true)
    public UserReponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Kiểm tra xem người dùng có phải là SUPER_ADMIN hay không
        boolean isSuperAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getRoleId() == 1); // SUPER_ADMIN có roleId = 1

        // Kiểm tra xem người dùng có phải là ADMIN hay không
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getRoleId() == 2); // ADMIN có roleId = 2

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<User> pageUsers;

        if (isSuperAdmin) {
            // SUPER_ADMIN xem được tất cả người dùng
            pageUsers = userRepo.findAll(pageDetails);
        } else if (isAdmin) {
            // ADMIN chỉ xem người dùng có role USER
            pageUsers = userRepo.findByRoles_RoleId(3, pageDetails); // Giả sử roleId = 3 là USER
        } else {
            // Người dùng thường chỉ xem chính mình
            List<User> currentUserList = List.of(currentUser);
            pageUsers = new PageImpl<>(currentUserList, pageDetails, 1);
        }

        List<UserDTO> userDTOS = pageUsers.getContent().stream()
                .map(this::convertToUserDTO) // dùng hàm convert đã viết
                .collect(Collectors.toList());

        return new UserReponse(
                userDTOS,
                pageUsers.getNumber(),
                pageUsers.getSize(),
                pageUsers.getTotalElements(),
                pageUsers.getTotalPages(),
                pageUsers.isLast());
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        // Kiểm tra nếu email mới đã tồn tại và không phải của chính user này
        if (!user.getEmail().equals(userDTO.getEmail()) && userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new APIException("Email đã tồn tại: " + userDTO.getEmail());
        }

        // Cập nhật thông tin cơ bản
        user.setFullname(userDTO.getFullname());
        user.setPhone(userDTO.getPhone());
        user.setUsername(userDTO.getUsername());
        user.setGender(userDTO.getGender());

        // Nếu email thay đổi, cập nhật vào user
        if (!user.getEmail().equals(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }

        // Cập nhật password nếu có
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Cập nhật vai trò (roles)
        Set<Role> newRoles = userDTO.getRoleIds().stream()
                .map(roleId -> roleRepo.findById(roleId)
                        .orElseThrow(() -> new APIException("Role not found with id: " + roleId)))
                .collect(Collectors.toSet());
        user.setRoles(newRoles);

        // Cập nhật địa chỉ
        if (userDTO.getAddresses() != null) {
            List<Address> existingAddresses = user.getAddresses();
            for (int i = 0; i < userDTO.getAddresses().size(); i++) {
                AddressDTO addressDTO = userDTO.getAddresses().get(i);
                if (i < existingAddresses.size()) {
                    Address existingAddress = existingAddresses.get(i);
                    existingAddress.setStreet(addressDTO.getStreet());
                    existingAddress.setBuildingName(addressDTO.getBuildingName());
                    existingAddress.setCity(addressDTO.getCity());
                    existingAddress.setState(addressDTO.getState());
                    existingAddress.setCountry(addressDTO.getCountry());
                    existingAddress.setPincode(addressDTO.getPincode());
                } else {
                    Address newAddress = new Address();
                    newAddress.setStreet(addressDTO.getStreet());
                    newAddress.setBuildingName(addressDTO.getBuildingName());
                    newAddress.setCity(addressDTO.getCity());
                    newAddress.setState(addressDTO.getState());
                    newAddress.setCountry(addressDTO.getCountry());
                    newAddress.setPincode(addressDTO.getPincode());
                    newAddress.setUsers(List.of(user));
                    existingAddresses.add(newAddress);
                }
            }
            user.setAddresses(existingAddresses);
        }

        // Cập nhật giỏ hàng (cart)
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
        }
        if (userDTO.getCart() != null) {
            cart.setTotalPrice(userDTO.getCart().getTotalPrice());
        }
        cart.setUser(user);
        user.setCart(cart);

        // Lưu user (cascade sẽ tự động lưu cart và addresses)
        User updatedUser = userRepo.save(user);

        // Trả về DTO sau khi chuyển đổi
        return convertToUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        // Xóa quan hệ giữa user và address nhưng không xóa Address khỏi DB
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            user.getAddresses().forEach(address -> address.getUsers().remove(user));
        }

        // Xóa địa chỉ liên kết với user (nếu có)
        addressRepo.deleteAddressesByUserId(userId);

        // Xóa giỏ hàng của user và các mục trong giỏ hàng
        Cart cart = cartRepo.findCartByUserId(userId);
        if (cart != null) {
            cartItemRepo.deleteCartItemsByCartId(cart.getCartId()); // Xóa tất cả sản phẩm trong giỏ hàng
            cartRepo.delete(cart); // Xóa giỏ hàng của user
        }

        // Cuối cùng, xóa user
        userRepo.delete(user);

        return "User with ID " + userId + " has been deleted successfully!";
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setFullname(user.getFullname());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setPhone(user.getPhone());
        userDTO.setGender(user.getGender());
        // Không set password vì không trả về mật khẩu

        // Set roleIds
        userDTO.setRoleIds(user.getRoles().stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet()));

        // Set addresses
        if (user.getAddresses() != null) {
            List<AddressDTO> addressDTOs = user.getAddresses().stream()
                    .map(address -> modelMapper.map(address, AddressDTO.class))
                    .collect(Collectors.toList());
            userDTO.setAddresses(addressDTOs);
        }

        // Set cart
        if (user.getCart() != null) {
            CartDTO cartDTO = cartService.convertCartToDTO(user.getCart());
            userDTO.setCart(cartDTO);
        }

        // Set favorites
        if (user.getFavorites() != null) {
            List<FavoriteDTO> favoriteDTOs = user.getFavorites().stream()
                    .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
                    .collect(Collectors.toList());
            userDTO.setFavorites(favoriteDTOs);
        }

        // Set orders
        if (user.getOrders() != null) {
            List<OrderDTO> orderDTOs = user.getOrders().stream()
                    .map(order -> modelMapper.map(order, OrderDTO.class))
                    .collect(Collectors.toList());
            userDTO.setOrders(orderDTOs);
        }

        return userDTO;
    }

}
