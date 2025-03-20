package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String email;
    private String token;
}
