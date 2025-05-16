package com.backend.backend_java.entity;

public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    // Getter và Setter cho token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter và Setter cho newPassword
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
