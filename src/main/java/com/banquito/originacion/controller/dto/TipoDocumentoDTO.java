package com.banquito.originacion.controller.dto;

import com.banquito.originacion.enums.EstadoTiposDocumentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de tipos de documentos")
public class TipoDocumentoDTO {

    @Schema(description = "Identificador único del tipo de documento", example = "1")
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 40, message = "El nombre no puede exceder 40 caracteres")
    @Schema(description = "Nombre del tipo de documento", example = "Cédula de Identidad", maxLength = 40)
    private String nombre;

    @NotBlank(message = "La descripción es requerida")
    @Size(max = 150, message = "La descripción no puede exceder 150 caracteres")
    @Schema(description = "Descripción detallada del tipo de documento", example = "Documento de identificación personal emitido por el Registro Civil", maxLength = 150)
    private String descripcion;

    @NotNull(message = "El estado es requerido")
    @Schema(description = "Estado del tipo de documento", example = "ACTIVO")
    private EstadoTiposDocumentoEnum estado;

    @NotNull(message = "La versión es requerida")
    @Min(value = 0, message = "La versión no puede ser negativa")
    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
} 