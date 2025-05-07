package com.backend.backend_java.config;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.backend.backend_java.entity.Role;
import com.backend.backend_java.entity.User;
import com.backend.backend_java.enums.AuthProvider;
import com.backend.backend_java.repository.RoleRepo;
import com.backend.backend_java.repository.UserRepo;
import com.backend.backend_java.security.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public OAuth2SuccessHandler(JWTUtil jwtUtil, UserRepo userRepo, RoleRepo roleRepo,
            PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // DEBUG: In toàn bộ thông tin user để kiểm tra
        System.out.println("OAuth2 User Attributes: " + oAuth2User.getAttributes());

        String rawEmail = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Xử lý khi Google không trả về email (rất hiếm, do thiếu scope)
        if (rawEmail == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found in OAuth2 response");
            return;
        }

        // Đảm bảo có @gmail.com nếu chưa có
        final String email = rawEmail.contains("@") ? rawEmail : rawEmail + "@gmail.com";

        // Tạo user nếu chưa tồn tại
        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setFullname(name != null ? name : "Google User");
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // password ngẫu nhiên
            newUser.setAuthProvider(AuthProvider.GOOGLE);

            // Gán quyền USER mặc định (chắc chắn tồn tại)
            Role userRole = roleRepo.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            newUser.setRoles(Set.of(userRole));

            return userRepo.save(newUser);
        });

        // Tạo JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Redirect về frontend kèm token
        response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + token);
    }
}
