package com.application.blank.util;

import com.application.blank.security.entity.Rol;
import com.application.blank.security.enums.RolName;
import com.application.blank.security.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * MUY IMPORTANTE: ESTA CLASE SÓLO SE EJECUTARÁ UNA VEZ PARA CREAR LOS ROLES.
 * UNA VEZ CREADOS SE DEBERÁ ELIMINAR O BIEN COMENTAR EL CÓDIGO
 */

@Component
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {
        /**
         * Sección a descomentar para crear roles
         */

/*
        Rol rolAdmin = new Rol(RolName.ROLE_ADMIN);
         Rol rolUser = new Rol(RolName.ROLE_USER);
         rolService.save(rolAdmin);
         rolService.save(rolUser);
 */

    }
}
