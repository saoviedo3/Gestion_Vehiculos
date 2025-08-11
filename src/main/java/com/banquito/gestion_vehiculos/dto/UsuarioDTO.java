package com.banquito.gestion_vehiculos.dto;

import com.banquito.gestion_vehiculos.enums.RolEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de usuarios")
public class UsuarioDTO {

    @Schema(description = "ID generado automáticamente", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Schema(description = "Email del usuario", example = "admin@automax.com")
    private String email;

    @Schema(description = "Contraseña (solo para creación)", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @NotNull(message = "El rol es requerido")
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private RolEnum rol;

    @Schema(description = "ID del vendedor asociado (solo para vendedores)")
    private String vendedorId;

    @Schema(description = "ID del concesionario asociado (solo para vendedores)")
    private String concesionarioId;

    @Schema(description = "Estado del usuario", example = "true")
    private boolean activo = true;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;
} 