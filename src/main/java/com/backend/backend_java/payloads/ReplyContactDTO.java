package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyContactDTO {
    private Long replyId;
    private String replyContent;
    private String createdAt;
    private Long contactId; // Chỉ lấy ID của Contact
    private Long userId; // Chỉ lấy ID của User (người trả lời)
}
