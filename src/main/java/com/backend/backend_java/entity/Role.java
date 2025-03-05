package com.backend.backend_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Boolean roleStatus; // true: active, false: inactive

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;
}
