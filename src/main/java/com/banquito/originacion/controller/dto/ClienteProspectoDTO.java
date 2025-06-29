package com.banquito.originacion.controller.dto;

import com.banquito.originacion.enums.EstadoClientesEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de clientes y prospectos")
public class ClienteProspectoDTO {

    @Schema(description = "Identificador único del cliente/prospecto", example = "1")
    private Integer id;

    @NotBlank(message = "La cédula es requerida")
    @Size(max = 10, message = "La cédula no puede exceder 10 caracteres")
    @Pattern(regexp = "^[0-9]{10}$", message = "La cédula debe tener exactamente 10 dígitos")
    @Schema(description = "Cédula de identidad del cliente", example = "1234567890", maxLength = 10)
    private String cedula;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Schema(description = "Nombre del cliente", example = "Juan", maxLength = 50)
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(max = 50, message = "El apellido no puede exceder 50 caracteres")
    @Schema(description = "Apellido del cliente", example = "Pérez", maxLength = 50)
    private String apellido;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Teléfono de contacto", example = "0987654321", maxLength = 20)
    private String telefono;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 60, message = "El email no puede exceder 60 caracteres")
    @Schema(description = "Correo electrónico", example = "juan.perez@email.com", maxLength = 60)
    private String email;

    @NotBlank(message = "La dirección es requerida")
    @Size(max = 120, message = "La dirección no puede exceder 120 caracteres")
    @Schema(description = "Dirección de residencia", example = "Av. Amazonas N24-123 y Colón", maxLength = 120)
    private String direccion;

    @NotNull(message = "Los ingresos son requeridos")
    @DecimalMin(value = "0.0", message = "Los ingresos no pueden ser negativos")
    @DecimalMax(value = "9999999999.99", message = "Los ingresos exceden el límite permitido")
    @Schema(description = "Ingresos mensuales", example = "2500.00")
    private BigDecimal ingresos;

    @NotNull(message = "Los egresos son requeridos")
    @DecimalMin(value = "0.0", message = "Los egresos no pueden ser negativos")
    @DecimalMax(value = "9999999999.99", message = "Los egresos exceden el límite permitido")
    @Schema(description = "Egresos mensuales", example = "1200.00")
    private BigDecimal egresos;

    @NotBlank(message = "La actividad económica es requerida")
    @Size(max = 120, message = "La actividad económica no puede exceder 120 caracteres")
    @Schema(description = "Actividad económica del cliente", example = "Ingeniero de Software", maxLength = 120)
    private String actividadEconomica;

    @NotNull(message = "El estado es requerido")
    @Schema(description = "Estado del cliente en el sistema", example = "ACTIVO")
    private EstadoClientesEnum estado;

    
  
    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
} 