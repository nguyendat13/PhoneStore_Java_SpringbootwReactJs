package com.backend.backend_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String position; // Vị trí hiển thị banner

    private String description;

    @Column(nullable = false)
    private Integer sortOrder; // Thứ tự hiển thị

    @Column(nullable = false)
    private String image;
}
