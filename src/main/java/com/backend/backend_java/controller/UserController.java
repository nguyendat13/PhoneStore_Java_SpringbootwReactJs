package com.backend.backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend_java.config.AppConstants;
import com.backend.backend_java.payloads.ProductResponse;
import com.backend.backend_java.payloads.UserDTO;
import com.backend.backend_java.payloads.UserReponse;
import com.backend.backend_java.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/public/users/email/{email}")
    public ResponseEntity<UserDTO> getUserEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
    }

    @GetMapping("/public/users")
    public ResponseEntity<UserReponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        // Đảm bảo pageNumber không nhỏ hơn 0
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;

        // Kiểm tra các tham số sắp xếp
        String sortField = "id".equals(sortBy) ? "userId" : sortBy;
        String order = "asc".equalsIgnoreCase(sortOrder) ? "asc" : "desc"; // mặc định sắp xếp theo descending nếu không
                                                                           // xác định

        // Lấy dữ liệu từ service
        UserReponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortField, order);

        // Trả về dữ liệu với mã trạng thái OK
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/public/users/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);

        return new ResponseEntity<UserDTO>(user, HttpStatus.FOUND);
    }

    @PutMapping("/public/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String status = userService.deleteUser(userId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}