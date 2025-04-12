package com.application.blank.util;


import com.application.blank.security.entity.User;

public class UserResponse {
    private String mensaje;
    private User user;

    public UserResponse(String mensaje, User user) {
        this.mensaje = mensaje;
        this.user = user;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public User getUsuario() {
        return user;
    }

    public void setUsuario(User user) {
        this.user = user;
    }
}
