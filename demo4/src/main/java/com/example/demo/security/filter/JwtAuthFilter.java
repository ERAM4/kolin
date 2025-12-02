package com.example.demo.security.filter;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.security.jwt.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ⭐ PERMITIR ACCESO SIN TOKEN
        if (path.startsWith("/auth/")
                || path.startsWith("/api/usuarios")   // <-- REGISTRO Y CONSULTA DE USUARIOS
                || path.startsWith("/h2-console/")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);

                // ⭐ VALIDAR TOKEN
                if (jwtService.isTokenValid(token)) {
                    String username = jwtService.extractUsername(token);

                    // ⭐ CARGAR USUARIO
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("❌ Token inválido");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ No hay Authorization header o no empieza con 'Bearer '");
        }

        filterChain.doFilter(request, response);
    }
}