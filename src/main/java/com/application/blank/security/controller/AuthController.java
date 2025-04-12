package com.application.blank.security.controller;


import com.application.blank.security.dto.JwtDTO;
import com.application.blank.security.dto.LoginDTO;
import com.application.blank.security.dto.NewUserDTO;
import com.application.blank.security.entity.User;
import com.application.blank.security.service.UserService;
import com.application.blank.util.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/new")
    public ResponseEntity<UserResponse> nuevo(@Valid @RequestBody NewUserDTO newUserDTO) {
        UserResponse respuesta = userService.save(newUserDTO);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok(userService.login(loginDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAllTheUsers() {
        List<User> users = userService.getAllTheUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @Valid @RequestBody NewUserDTO newUserDTO) {
        User userActualizado = userService.updateUser(id, newUserDTO);
        return ResponseEntity.ok(userActualizado);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Integer id) {
        Map<String, Boolean> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDTO> refresh(@RequestBody JwtDTO jwtDTO) throws ParseException {
        return ResponseEntity.ok(userService.refresh(jwtDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            userService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Token no proporcionado"));
    }

}
