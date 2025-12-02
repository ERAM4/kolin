package com.example.demo.service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User saveUser(User user){
        user.setRol("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsuarios() {
        return userRepository.findAll();
    }

    public User findByCorreo(String correo) {
        return userRepository.findByCorreo(correo).orElse(null);
    }



    public User getUsuarioById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUsuario(Long id, User usuarioActualizado) {
        return userRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsername(usuarioActualizado.getUsername());
                    usuario.setCorreo(usuarioActualizado.getCorreo());
                    usuario.setPassword(usuarioActualizado.getPassword());
                    return userRepository.save(usuario);
                })
                .orElse(null);
    }

    public void deleteUsuario(Long id) {
        userRepository.deleteById(id);
    }
}
