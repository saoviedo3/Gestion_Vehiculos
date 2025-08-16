package com.banquito.gestion_vehiculos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Obtener el email del header (el frontend enviará el email del usuario logueado)
        String userEmail = request.getHeader("X-User-Email");
        
        if (userEmail != null && !userEmail.isEmpty()) {
            // Crear un UserDetails simple para Spring Security
            UserDetails userDetails = User.builder()
                    .username(userEmail)
                    .password("") // No necesitamos la contraseña aquí
                    .authorities("USER") // Rol básico
                    .build();
            
            // Crear el token de autenticación
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            // Establecer la autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
} 