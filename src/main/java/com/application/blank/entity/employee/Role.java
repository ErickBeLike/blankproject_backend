package com.application.blank.entity.employee;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long roleId;

    @Column(nullable = false)
    private String roleName;
    @Column
    private String roleDescription;

    @Column(updatable = false)
    private LocalDate createdAt;
    @Column
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public Role() {
    }

    public Role(String roleName, String roleDescription, LocalDate createdAt, LocalDate updatedAt) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
