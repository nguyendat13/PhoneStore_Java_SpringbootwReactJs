package com.backend.backend_java.controller;

import java.nio.file.attribute.UserPrincipal;

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

    @GetMapping("/public/users")
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