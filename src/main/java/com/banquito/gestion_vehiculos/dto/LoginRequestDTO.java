package com.banquito.gestion_vehiculos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para solicitudes de login")
public class LoginRequestDTO {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Schema(description = "Email del usuario", example = "admin@automax.com")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
} 