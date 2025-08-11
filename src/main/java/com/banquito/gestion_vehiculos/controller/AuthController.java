package com.banquito.gestion_vehiculos.controller;

import com.banquito.gestion_vehiculos.dto.LoginRequestDTO;
import com.banquito.gestion_vehiculos.dto.LoginResponseDTO;
import com.banquito.gestion_vehiculos.dto.UsuarioDTO;
import com.banquito.gestion_vehiculos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Autenticación", description = "Operaciones de autenticación y gestión de usuarios")
@RestController
@RequestMapping("/api/concesionarios/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con email y contraseña")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario (solo admin)")
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(authService.createUsuario(dto));
    }

    @Operation(summary = "Obtener usuario por email", description = "Obtiene un usuario usando su email")
    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<UsuarioDTO> getUsuarioByEmail(@PathVariable String email) {
        return ResponseEntity.ok(authService.findUsuarioByEmail(email));
    }

    @Operation(summary = "Listar todos los usuarios", description = "Obtiene todos los usuarios (solo admin)")
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(authService.findAllUsuarios());
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente")
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable String id, @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(authService.updateUsuario(id, dto));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String id) {
        authService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener perfil del usuario actual", description = "Obtiene el perfil del usuario autenticado")
    @GetMapping("/profile")
    public ResponseEntity<UsuarioDTO> getCurrentUserProfile(@RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(authService.findUsuarioByEmail(userEmail));
    }
} 