package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String title;
    private String description;
    private String content;
    private String image;
    private String type;
    private Long topicId;  // Chỉ lấy ID của topic để giảm tải dữ liệu
}
