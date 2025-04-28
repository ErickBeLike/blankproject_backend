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
import com.application.blank.service.file.StorageService;
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
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    StorageService storageService;

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

    public UserResponse save(NewUserDTO dto,
                             MultipartFile profileImage,
                             String baseUrl) {
        // 1) Validaciones de existencia
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "ese nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "ese correo ya está en uso");
        }

        // 2) Validación contraseña
        String rawPassword = dto.getPassword().trim();
        if (rawPassword.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "contraseña inválida");
        }

        // 3) Crear entidad User
        User user = new User(
                dto.getUserName(),
                dto.getEmail(),
                passwordEncoder.encode(rawPassword)
        );

        // 4) Asignar roles
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER)
                .orElseThrow(() -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "rol USER no encontrado")));
        if (dto.getRoles() != null && dto.getRoles().contains("admin")) {
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "rol ADMIN no encontrado")));
        }
        user.setRoles(roles);

        // 5) Procesar imagen de perfil
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String filename = storageService.saveFile(
                        profileImage,
                        profileImage.getOriginalFilename(),
                        "profileimages"
                );
                // ej. http://mi-dominio.com/mediafiles/profileimages/uuid.png
                String imageUrl = baseUrl + "/mediafiles/profileimages/" + filename;
                user.setProfilePictureUrl(imageUrl);
            } catch (Exception e) {
                throw new CustomException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error al procesar imagen de perfil: " + e.getMessage()
                );
            }
        }

        // 6) Persistir y devolver respuesta
        userRepository.save(user);
        return new UserResponse(user.getUserName() + " ha sido creado");
    }


    public List<User> getAllTheUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "No se encontró un usuario para el ID: " + id));
    }

    public UserResponse updateUser(Long id,
                                   NewUserDTO dto,
                                   MultipartFile profileImage,
                                   String baseUrl) {

        // 1) Traer usuario existente
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró un usuario para el ID: " + id
                ));

        // 2) Validar y actualizar email
        String newEmail = dto.getEmail().trim();
        if (newEmail.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "email inválido");
        }
        if (!newEmail.equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new CustomException(
                        HttpStatus.BAD_REQUEST,
                        "ese correo ya está en uso por otro usuario"
                );
            }
            user.setEmail(newEmail);
        }

        // 3) Validar y actualizar contraseña
        String rawPassword = dto.getPassword().trim();
        if (rawPassword.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "contraseña inválida");
        }
        user.setPassword(passwordEncoder.encode(rawPassword));

        // 4) Actualizar userName
        user.setUserName(dto.getUserName());

        // 5) Actualizar roles
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "rol USER no encontrado"
                )));
        if (dto.getRoles() != null && dto.getRoles().contains("admin")) {
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "rol ADMIN no encontrado"
                    )));
        }
        user.setRoles(roles);

        // 6) Borrar imagen anterior si existe…
        if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isBlank()) {
            String prev = user.getProfilePictureUrl();
            int slash = prev.lastIndexOf('/');
            if (slash >= 0 && slash < prev.length() - 1) {
                String oldFilename = prev.substring(slash + 1);
                storageService.deleteFile(oldFilename, "profileimages");
            }
        }

        // 7) Procesar nueva imagen
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String filename = storageService.saveFile(
                        profileImage,
                        profileImage.getOriginalFilename(),
                        "profileimages"
                );
                String imageUrl = baseUrl + "/mediafiles/profileimages/" + filename;
                user.setProfilePictureUrl(imageUrl);
            } catch (Exception e) {
                throw new CustomException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error al procesar imagen de perfil: " + e.getMessage()
                );
            }
        }

        // 8) Fecha de actualización
        user.setUpdatedAt(LocalDateTime.now());

        // 9) Guardar y devolver solo mensaje
        userRepository.save(user);
        return new UserResponse(user.getUserName() + " ha sido actualizado");
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
