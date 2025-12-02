package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Gestion de Usuarios")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public List<User> getAllUsuarios() {
        return userService.findAllUsuarios();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener a un usuario por el id")
    public User getUsuarioById(@PathVariable Long id) {
        return userService.getUsuarioById(id);
    }

    @PostMapping
    @Operation(summary = "Crea a un Usuario")
    public User saveUser(@RequestBody User usuario) {
        return userService.saveUser(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un Usuario")
    public User updateUsuario(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUsuario(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar a un Usuario")
    public void deleteUsuario(@PathVariable Long id) {
        userService.deleteUsuario(id);
    }

}
