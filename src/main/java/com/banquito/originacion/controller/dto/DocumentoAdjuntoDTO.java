package com.banquito.originacion.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de documentos adjuntos")
public class DocumentoAdjuntoDTO {

    @Schema(description = "Identificador único del documento adjunto", example = "1")
    private Integer id;

    @NotNull(message = "El ID de la solicitud es requerido")
    @Min(value = 1, message = "El ID de la solicitud debe ser mayor a 0")
    @Schema(description = "Identificador de la solicitud de crédito", example = "1")
    private Integer idSolicitud;

    @NotNull(message = "El ID del tipo de documento es requerido")
    @Min(value = 1, message = "El ID del tipo de documento debe ser mayor a 0")
    @Schema(description = "Identificador del tipo de documento", example = "1")
    private Integer idTipoDocumento;

    @NotBlank(message = "La ruta del archivo es requerida")
    @Size(max = 150, message = "La ruta del archivo no puede exceder 150 caracteres")
    @Schema(description = "Ruta donde se almacena el archivo", example = "/documentos/solicitudes/2024/cedula_1234567890.pdf", maxLength = 150)
    private String rutaArchivo;

    @NotNull(message = "La fecha de carga es requerida")
    @Schema(description = "Fecha y hora cuando se cargó el documento", example = "2024-01-15T14:30:00")
    private LocalDateTime fechaCargado;

    
    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
} 