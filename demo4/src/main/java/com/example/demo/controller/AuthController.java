package com.example.demo.controller;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String correo = request.get("correo");
        String password = request.get("password");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, password)
        );

        if (!auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(correo);

        return ResponseEntity.ok(Map.of(
                "token", token
        ));
    }
}
