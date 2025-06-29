package com.banquito.originacion.controller.dto;

import com.banquito.originacion.enums.EstadoVehiculoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de vehículos")
public class VehiculoDTO {

    @Schema(description = "Identificador único del vehículo", example = "1")
    private Integer id;

    @NotNull(message = "El ID del concesionario es requerido")
    @Min(value = 1, message = "El ID del concesionario debe ser mayor a 0")
    @Schema(description = "Identificador del concesionario", example = "1")
    private Integer idConcesionario;

    @NotNull(message = "El ID del identificador de vehículo es requerido")
    @Min(value = 1, message = "El ID del identificador de vehículo debe ser mayor a 0")
    @Schema(description = "Identificador del identificador de vehículo", example = "1")
    private Integer idIdentificadorVehiculo;

    @NotBlank(message = "La marca es requerida")
    @Size(max = 40, message = "La marca no puede exceder 40 caracteres")
    @Schema(description = "Marca del vehículo", example = "Toyota", maxLength = 40)
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    @Size(max = 40, message = "El modelo no puede exceder 40 caracteres")
    @Schema(description = "Modelo del vehículo", example = "Corolla", maxLength = 40)
    private String modelo;

    @NotNull(message = "El año es requerido")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2030, message = "El año no puede ser mayor al año actual más 5")
    @Schema(description = "Año de fabricación del vehículo", example = "2023")
    private Integer anio;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "0.0", message = "El valor no puede ser negativo")
    @DecimalMax(value = "99999999.99", message = "El valor excede el límite permitido")
    @Schema(description = "Valor comercial del vehículo", example = "25000.00")
    private BigDecimal valor;

    @NotBlank(message = "El color es requerido")
    @Size(max = 30, message = "El color no puede exceder 30 caracteres")
    @Schema(description = "Color del vehículo", example = "Blanco", maxLength = 30)
    private String color;

    @Size(max = 150, message = "Los extras no pueden exceder 150 caracteres")
    @Schema(description = "Extras del vehículo", example = "Aire acondicionado, sistema de sonido premium", maxLength = 150)
    private String extras;

    @NotNull(message = "El estado es requerido")
    @Schema(description = "Estado del vehículo", example = "NUEVO")
    private EstadoVehiculoEnum estado;

    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
} 