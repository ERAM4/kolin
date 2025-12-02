package com.example.demo.service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getCorreo())
                .password(user.getPassword())
                .roles(user.getRol())
                .build();
    }
}
