package com.application.blank.dto.employee;

import jakarta.validation.constraints.NotBlank;

public class RoleDTO {

    private Long roleId;

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String roleDescription;

    public RoleDTO() {
    }

    public RoleDTO(Long roleId, String roleName, String roleDescription) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
