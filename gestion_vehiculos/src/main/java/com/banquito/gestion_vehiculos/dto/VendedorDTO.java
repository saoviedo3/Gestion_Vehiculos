package com.banquito.gestion_vehiculos.dto;

import com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de vendedores")
public class VendedorDTO {

    @Schema(description = "Identificador único del vendedor", example = "1")
    private String id;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre completo del vendedor", example = "Carlos Rodríguez", maxLength = 100)
    private String nombre;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Teléfono de contacto del vendedor", example = "0987654321", maxLength = 20)
    private String telefono;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 60, message = "El email no puede exceder 60 caracteres")
    @Schema(description = "Correo electrónico del vendedor", example = "carlos.rodriguez@automax.com", maxLength = 60)
    private String email;

    @NotNull(message = "El estado es requerido")
    @Schema(description = "Estado del vendedor", example = "ACTIVO")
    private EstadoVendedorEnum estado;

    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
}
