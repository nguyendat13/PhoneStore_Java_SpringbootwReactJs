package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {
    private Long bannerId;
    private String title;
    private String position;
    private String description;
    private Integer sortOrder;
    private String image;
}
