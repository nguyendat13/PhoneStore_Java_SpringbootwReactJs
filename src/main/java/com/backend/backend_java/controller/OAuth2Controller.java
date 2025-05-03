package com.backend.backend_java.controller;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend_java.entity.Role;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.payloads.AuthResponse;
import com.backend.backend_java.repository.RoleRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.security.JWTUtil;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @GetMapping("/success")
    public ResponseEntity<?> getOauth2LoginInfo(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Tìm hoặc tạo user
        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setFullname(name);
            newUser.setPassword(""); // Không cần mật khẩu

            Optional<Role> roleOptional = roleRepo.findByRoleName("USER");
            if (roleOptional.isEmpty()) {
                throw new RuntimeException("Role USER not found");
            }
            newUser.setRoles(Set.of(roleOptional.get()));
            return userRepo.save(newUser);
        });

        // Tạo JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(new AuthResponse(user.getUserId(), user.getEmail(), token,
                user.getRoles().stream().map(r -> r.getRoleName()).toList()));
    }
}
