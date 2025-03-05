package com.backend.backend_java.payloads;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private List<CategoryDTO> content; // Đổi từ UserDTO thành CategoryDTO
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;

    // Không cần setter tùy chỉnh vì Lombok sẽ tự động tạo setter/getter
}
