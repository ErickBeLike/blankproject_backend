package com.application.blank.security.dto;


import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank(message = "nombre de usuario/email obligatorio")
    private String userName;
    @NotBlank(message = "contraseña obligatoria")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
