package com.backend.backend_java.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.backend.backend_java.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String fullname;
    private String email;
    private String username;
    private String phone;
    private String gender;
    private String password;
    private Set<Long> roleIds = new HashSet<>();
    private List<AddressDTO> addresses;
    private List<CartDTO> carts;
    private List<FavoriteDTO> favorites;
    private List<OrderDTO> orders;
}
