package com.banquito.originacion.controller.dto;

import com.banquito.originacion.enums.AccionAuditoriaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de auditorías del sistema")
public class AuditoriaDTO {

    @Schema(description = "Identificador único de la auditoría", example = "1")
    private Integer id;

    @NotBlank(message = "El nombre de la tabla es requerido")
    @Size(max = 40, message = "El nombre de la tabla no puede exceder 40 caracteres")
    @Schema(description = "Nombre de la tabla auditada", example = "clientes_prospectos", maxLength = 40)
    private String tabla;

    @NotNull(message = "La acción es requerida")
    @Schema(description = "Acción realizada en la auditoría", example = "INSERT")
    private AccionAuditoriaEnum accion;

    @NotNull(message = "La fecha y hora son requeridas")
    @Schema(description = "Fecha y hora de la acción auditada", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaHora;
} 