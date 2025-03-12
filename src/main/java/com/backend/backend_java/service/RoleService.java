package com.backend.backend_java.service;

import com.backend.backend_java.payloads.RoleDTO;
import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO getRoleById(Long roleId);

    List<RoleDTO> getAllRoles();

    RoleDTO updateRole(Long roleId, RoleDTO roleDTO);

    String deleteRole(Long roleId);
}
