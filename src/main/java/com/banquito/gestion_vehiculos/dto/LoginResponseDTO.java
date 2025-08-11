package com.banquito.gestion_vehiculos.dto;

import com.banquito.gestion_vehiculos.enums.RolEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para respuestas de login")
public class LoginResponseDTO {

    @Schema(description = "Mensaje de confirmación", example = "Login exitoso")
    private String mensaje;

    @Schema(description = "Rol del usuario autenticado", example = "ADMIN")
    private RolEnum rol;

    @Schema(description = "ID del vendedor (solo para vendedores)", example = "vendedor123")
    private String vendedorId;

    @Schema(description = "ID del concesionario (solo para vendedores)", example = "concesionario456")
    private String concesionarioId;

    @Schema(description = "Email del usuario", example = "admin@automax.com")
    private String email;
} 