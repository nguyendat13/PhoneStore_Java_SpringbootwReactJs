package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private Long contactId;
    private String fullname;
    private String email;
    private String phone;
    private String title;
    private String content;
    private String status;
    private String createdAt;
    private Long userId; // Chỉ lấy ID của User
}
