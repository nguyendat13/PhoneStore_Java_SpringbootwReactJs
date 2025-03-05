package com.backend.backend_java.service;

import com.backend.backend_java.payloads.UserDTO;
import com.backend.backend_java.payloads.UserReponse;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    UserDTO getUserByEmail(String email); // Đã thêm phương thức này

    UserReponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);
}
