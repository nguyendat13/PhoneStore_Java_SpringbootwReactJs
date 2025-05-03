package com.backend.backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.entity.User;
import com.backend.backend_java.payloads.AuthResponse;
import com.backend.backend_java.payloads.LoginRequest;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.security.JWTUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JWTUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepo.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại!");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = optionalUser.get();

            String token = jwtUtil.generateToken(user.getEmail());

            // Lấy danh sách role name từ user
            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getRoleName()) // giữ nguyên hoặc .toUpperCase() nếu bạn muốn
                    .toList();

            return ResponseEntity.ok(new AuthResponse(user.getUserId(), user.getEmail(), token, roles));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu không đúng!");
        }
    }

}
