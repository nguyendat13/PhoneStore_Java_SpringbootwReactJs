package com.backend.backend_java.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend_java.entity.Role;
import com.backend.backend_java.exceptions.ResourceNotFoundException;
import com.backend.backend_java.payloads.RoleDTO;
import com.backend.backend_java.repository.RoleRepo;
import com.backend.backend_java.service.RoleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role savedRole = roleRepo.save(role);
        return modelMapper.map(savedRole, RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));

        role.setRoleName(roleDTO.getRoleName());
        role.setRoleStatus(roleDTO.getRoleStatus());

        Role updatedRole = roleRepo.save(role);
        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public String deleteRole(Long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));
        roleRepo.delete(role);
        return "Role with roleId " + roleId + " deleted successfully!";
    }
}
