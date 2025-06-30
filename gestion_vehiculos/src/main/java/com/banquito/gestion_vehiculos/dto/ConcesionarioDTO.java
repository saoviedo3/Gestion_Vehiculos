package com.banquito.gestion_vehiculos.dto;

import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de concesionarios")
public class ConcesionarioDTO {

    @Schema(description = "Identificador único del concesionario", example = "1")
    private String id;

    @NotBlank(message = "La razón social es requerida")
    @Size(max = 80, message = "La razón social no puede exceder 80 caracteres")
    @Schema(description = "Razón social del concesionario", example = "Concesionario AutoMax S.A.", maxLength = 80)
    private String razonSocial;

    @NotBlank(message = "La dirección es requerida")
    @Size(max = 120, message = "La dirección no puede exceder 120 caracteres")
    @Schema(description = "Dirección del concesionario", example = "Av. 6 de Diciembre N33-35 y Bosmediano", maxLength = 120)
    private String direccion;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Teléfono de contacto", example = "02-2234567", maxLength = 20)
    private String telefono;

    @NotBlank(message = "El email de contacto es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 50, message = "El email no puede exceder 50 caracteres")
    @Schema(description = "Correo electrónico de contacto", example = "contacto@automax.com", maxLength = 50)
    private String emailContacto;

    @NotNull(message = "El estado es requerido")
    @Schema(description = "Estado del concesionario", example = "ACTIVO")
    private EstadoConcesionarioEnum estado;

    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
}
