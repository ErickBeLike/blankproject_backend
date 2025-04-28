package com.application.blank.util;


import com.application.blank.security.entity.User;

public class UserResponse {
    private String mensaje;

    public UserResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
