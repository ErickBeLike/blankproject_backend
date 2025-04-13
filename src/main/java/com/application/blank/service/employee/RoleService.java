package com.application.blank.service.employee;

import com.application.blank.dto.employee.RoleDTO;
import com.application.blank.entity.employee.Role;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.employee.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long id) throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return mapToDTO(role);
    }

    public RoleDTO saveRole(RoleDTO roleDTO) {
        Role saved = roleRepository.save(mapToEntity(roleDTO));
        return mapToDTO(saved);
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) throws ResourceNotFoundException {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        existing.setRoleName(roleDTO.getRoleName());
        existing.setRoleDescription(roleDTO.getRoleDescription());
        return mapToDTO(roleRepository.save(existing));
    }

    public Map<String, Boolean> deleteRole(Long id) throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));

        roleRepository.delete(role);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    private RoleDTO mapToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        dto.setRoleDescription(role.getRoleDescription());
        return dto;
    }

    private Role mapToEntity(RoleDTO dto) {
        Role role = new Role();
        role.setRoleId(dto.getRoleId());
        role.setRoleName(dto.getRoleName());
        role.setRoleDescription(dto.getRoleDescription());
        return role;
    }
}
