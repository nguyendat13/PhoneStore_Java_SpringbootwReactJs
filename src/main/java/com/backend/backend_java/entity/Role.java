package com.backend.backend_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true)
    private String roleName; // Ví dụ: "ADMIN", "USER"

    @Column(nullable = false)
    private Integer roleStatus; // 0 = Super Admin, 1 = Admin, 2 = User (càng nhỏ quyền càng cao)

    // @Version
    // private Long version; // Thêm phiên bản cho entity này
}
