package com.application.blank.controller.employee;

import com.application.blank.dto.employee.RoleDTO;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.service.employee.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) throws ResourceNotFoundException {
        RoleDTO roleDTO = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO newRoleDTO = roleService.saveRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoleDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO)
            throws ResourceNotFoundException {
        RoleDTO updatedRoleDTO = roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(updatedRoleDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteRole(@PathVariable Long id) throws ResourceNotFoundException {
        return roleService.deleteRole(id);
    }
}
