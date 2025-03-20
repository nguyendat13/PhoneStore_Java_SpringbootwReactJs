package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Tự động tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor      // Constructor không tham số
@AllArgsConstructor     // Constructor đầy đủ tham số
public class LoginRequest {
    private String email;
    private String password;
}
