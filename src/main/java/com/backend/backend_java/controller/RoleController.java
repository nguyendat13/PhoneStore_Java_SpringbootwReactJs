package com.backend.backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.payloads.RoleDTO;
import com.backend.backend_java.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/admin/roles")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.createRole(roleDTO), HttpStatus.CREATED);
    }

    @GetMapping("/public/roles/{roleId}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long roleId) {
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.OK);
    }

    @GetMapping("/public/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @PutMapping("/admin/roles/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.updateRole(roleId, roleDTO), HttpStatus.OK);
    }

    @DeleteMapping("/admin/roles/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
        return new ResponseEntity<>(roleService.deleteRole(roleId), HttpStatus.OK);
    }
}
