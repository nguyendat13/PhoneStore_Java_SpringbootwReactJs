package com.backend.backend_java.controller;

import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.backend.backend_java.entity.PasswordResetToken;
import com.backend.backend_java.entity.ResetPasswordRequest;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.UserDTO;
import com.backend.backend_java.payloads.UserReponse;
import com.backend.backend_java.repository.PasswordResetTokenRepository;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepository;

    @PostMapping("/public/users/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/public/users/email/{email}")
    public ResponseEntity<UserDTO> getUserEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<UserReponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        // Validate and adjust parameters
        pageNumber = Math.max(0, pageNumber - 1); // Ensure pageNumber starts from 0
        pageSize = Math.max(1, pageSize); // Ensure minimum page size is 1

        // Normalize sort parameters
        String sortField = "id".equals(sortBy) ? "userId" : sortBy;
        String order = "asc".equalsIgnoreCase(sortOrder) ? "asc" : "desc";

        // Get data from service
        UserReponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortField, order);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/public/users/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);

        if (user == null) {
            // Nếu không tìm thấy người dùng, trả về 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Nếu tìm thấy người dùng, trả về 200 OK và thông tin người dùng
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/public/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            // Call service to delete the user
            userService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // If user is not found, return NOT_FOUND status
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Catch any other exceptions and return internal server error
            return new ResponseEntity<>("An error occurred while deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/public/users/request-password-reset")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        userService.sendResetPasswordEmail(email);
        return ResponseEntity.ok(Map.of("message", "Check your email for password reset link"));
    }

    @PostMapping("/public/users/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(request.getToken());

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        PasswordResetToken token = tokenOpt.get();
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }

        Optional<User> userOpt = userRepository.findByEmail(token.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            tokenRepository.delete(token); // Xóa token sau khi dùng
            return ResponseEntity.ok("Password updated successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // mới thêm này để khớp frontend
    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        userService.sendResetPasswordEmail(email);
        return ResponseEntity.ok(Map.of("message", "Check your email for reset link"));
    }
}