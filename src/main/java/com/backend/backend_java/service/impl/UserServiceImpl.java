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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            //     throw new ResourceAlreadyExistsException("Username đã tồn tại!");
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


            // // Thêm CartItem mặc định (nếu cần)
            // List<Product> defaultProducts = productRepo.findDefaultProducts(); // Lấy danh sách sản phẩm mặc định
            // List<CartItem> cartItems = new ArrayList<>();

            // for (Product product : defaultProducts) {
            //     CartItem cartItem = new CartItem();
            //     cartItem.setCart(cart);
            //     cartItem.setProduct(product);
            //     cartItem.setQuantity(1); // Mặc định 1 sản phẩm
            //     cartItem.setDiscount(0.0);
            //     cartItem.setProductPrice(product.getPrice());
            //     cartItems.add(cartItem);
            // }
            // // Lưu danh sách CartItem vào database
            // cartItemRepo.saveAll(cartItems);


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

                    // Set cart giống như trong getUserById
                    if (user.getCart() != null) {
                        userDTO.setCart(modelMapper.map(user.getCart(), CartDTO.class));
                    }
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
                .collect(Collectors.toSet()));

        userDTO.setAddresses(user.getAddresses().stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList()));

        // Set cart with email
        if (user.getCart() != null) {
            CartDTO cartDTO = modelMapper.map(user.getCart(), CartDTO.class);
            cartDTO.setEmail(user.getEmail());
            userDTO.setCart(cartDTO);
        }

        userDTO.setFavorites(user.getFavorites().stream()
                .map(favorite -> modelMapper.map(favorite, FavoriteDTO.class))
                .collect(Collectors.toList()));

        userDTO.setOrders(user.getOrders().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList()));

        return userDTO;
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
    
        // // ✅ Nếu email thay đổi, cập nhật vào user và cart
        // if (!user.getEmail().equals(userDTO.getEmail())) {
        //     user.setEmail(userDTO.getEmail());
        //     if (user.getCart() != null) {
        //         user.getCart().setEmail(userDTO.getEmail()); // Đồng bộ email trong giỏ hàng
        //     }
        // }
        
     // ✅ Nếu email thay đổi, cập nhật vào user
     if (!user.getEmail().equals(userDTO.getEmail())) {
        user.setEmail(userDTO.getEmail());
    }

        // Cập nhật password nếu có
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    
        // ✅ Cập nhật vai trò (roles)
        Set<Role> newRoles = userDTO.getRoleIds().stream()
                .map(roleId -> roleRepo.findById(roleId)
                        .orElseThrow(() -> new APIException("Role not found with id: " + roleId)))
                .collect(Collectors.toSet());
        user.setRoles(newRoles);
    
          // ✅ **Cập nhật địa chỉ thay vì tạo mới**
    if (userDTO.getAddresses() != null) {
       // ✅ Lấy danh sách địa chỉ hiện có của user
List<Address> existingAddresses = user.getAddresses();

if (userDTO.getAddresses() != null) {
    for (int i = 0; i < userDTO.getAddresses().size(); i++) {
        AddressDTO addressDTO = userDTO.getAddresses().get(i);

        // ✅ Nếu đã có địa chỉ cũ, cập nhật thay vì tạo mới
        if (i < existingAddresses.size()) {
            Address existingAddress = existingAddresses.get(i);
            existingAddress.setStreet(addressDTO.getStreet());
            existingAddress.setBuildingName(addressDTO.getBuildingName());
            existingAddress.setCity(addressDTO.getCity());
            existingAddress.setState(addressDTO.getState());
            existingAddress.setCountry(addressDTO.getCountry());
            existingAddress.setPincode(addressDTO.getPincode());
        } else {
            // ✅ Nếu user có thêm địa chỉ mới, tạo mới
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
}

        // ✅ Lưu lại danh sách địa chỉ đã cập nhật
        user.setAddresses(existingAddresses);

    }

        // ✅ Cập nhật giỏ hàng (cart)
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(); // Tạo mới nếu user chưa có giỏ hàng
        }
    
        // Cập nhật thông tin giỏ hàng nếu có trong request
        if (userDTO.getCart() != null) {
            cart.setTotalPrice(userDTO.getCart().getTotalPrice());
        }
    
        cart.setUser(user);
        user.setCart(cart);
    
        // ✅ Lưu user (cascade sẽ tự động lưu cart và addresses)
        User updatedUser = userRepo.save(user);
    
        // ✅ Chuyển đổi sang DTO để trả về
        UserDTO responseDTO = modelMapper.map(updatedUser, UserDTO.class);
        responseDTO.setRoleIds(updatedUser.getRoles().stream().map(Role::getRoleId).collect(Collectors.toSet()));
    
        return responseDTO;
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
    
}
