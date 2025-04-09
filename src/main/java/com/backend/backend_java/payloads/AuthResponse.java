package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String email;
    private String token;
    private List<String> roles; // <-- Thêm roles vào response

}
