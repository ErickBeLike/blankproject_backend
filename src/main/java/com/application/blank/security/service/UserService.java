package com.application.blank.security.service;


import com.application.blank.exception.CustomException;
import com.application.blank.security.blacklist.TokenBlacklist;
import com.application.blank.security.dto.JwtDTO;
import com.application.blank.security.dto.LoginDTO;
import com.application.blank.security.dto.NewUserDTO;
import com.application.blank.security.entity.Rol;
import com.application.blank.security.entity.User;
import com.application.blank.security.enums.RolName;
import com.application.blank.security.jwt.JwtProvider;
import com.application.blank.security.repository.UserRepository;
import com.application.blank.util.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    TokenBlacklist tokenBlacklist;

    public Optional<User> getByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public boolean existsByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }

    public JwtDTO login(LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        return new JwtDTO(jwt);
    }

    public UserResponse save(NewUserDTO newUserDTO) {
        if (userRepository.existsByUserName(newUserDTO.getUserName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "ese nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(newUserDTO.getEmail())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "ese correo ya está en uso");
        }

        String contrasena = newUserDTO.getPassword().trim();
        if (contrasena.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "contraseña inválida");
        }

        User user = new User(
                newUserDTO.getUserName(),
                newUserDTO.getEmail(),
                passwordEncoder.encode(contrasena)
        );
        user.setEmail(newUserDTO.getEmail());

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());

        if (newUserDTO.getRoles().contains("admin")) {
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new UserResponse(user.getUserName() + " ha sido creado", user);
    }


    public List<User> getAllTheUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "No se encontró un usuario para el ID: " + id));
    }

    public User updateUser(Long id, NewUserDTO newUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "No se encontró un usuario para el ID: " + id));

        String password = newUserDTO.getPassword().trim();
        if (password.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "contraseña inválida");
        }

        user.setUserName(newUserDTO.getUserName());
        user.setPassword(passwordEncoder.encode(password));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());
        if (newUserDTO.getRoles().contains("admin")) {
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        }
        user.setRoles(roles);

        /**
         * Modificación de la fecha dde actualización
         */
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return user;
    }

    public Map<String, Boolean> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "No se encontró un usuario para el ID: " + id));

        userRepository.delete(user);

        Map<String, Boolean> response = new HashMap<>();
        response.put("eliminado", Boolean.TRUE);
        return response;
    }

    public JwtDTO refresh(JwtDTO jwtDTO) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDTO);
        return new JwtDTO(token);
    }

    public void logout(String token) {
        if (jwtProvider.validateToken(token)) {
            long exp = jwtProvider.getExpirationFromToken(token);
            tokenBlacklist.add(token, exp);
        }
    }

}
