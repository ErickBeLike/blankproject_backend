package com.application.blank.security.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class NewUserDTO {
    @NotBlank(message = "nombre de usuario obligatorio")
    private String userName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private Set<String> roles;

    public NewUserDTO() {
    }

    public NewUserDTO(String userName, String email, String password, Set<String> roles) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public @NotBlank(message = "nombre de usuario obligatorio") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "nombre de usuario obligatorio") String userName) {
        this.userName = userName;
    }

    public @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
