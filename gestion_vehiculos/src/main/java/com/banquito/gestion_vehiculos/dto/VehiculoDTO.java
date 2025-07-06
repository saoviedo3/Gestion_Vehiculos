package com.banquito.gestion_vehiculos.dto;

import java.math.BigDecimal;

import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.TipoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CombustibleVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CondicionVehiculoEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de vehículos")
public class VehiculoDTO {

    @Schema(description = "ID generado automáticamente", accessMode = Schema.AccessMode.READ_ONLY, example = "6863125741aecf5c57b57ed0")
    private String id;

    @NotBlank(message = "La marca es requerida")
    @Size(max = 40, message = "La marca no puede exceder 40 caracteres")
    @Schema(description = "Marca del vehículo", example = "Toyota", maxLength = 40)
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    @Size(max = 40, message = "El modelo no puede exceder 40 caracteres")
    @Schema(description = "Modelo del vehículo", example = "Corolla", maxLength = 40)
    private String modelo;

    @NotNull(message = "El cilindraje es requerido")
    @Schema(description = "Cilindraje del vehículo", example = "1.8")
    private double cilindraje;

    @NotNull(message = "El año es requerido")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2030, message = "El año no puede ser mayor al año actual más 5")
    @Schema(description = "Año de fabricación del vehículo", example = "2023")
    private String anio;

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
    @Schema(description = "Estado del vehículo", example = "DISPONIBLE, VENDIDO, NO_DISPONIBLE")
    private EstadoVehiculoEnum estado;

    @NotNull(message = "El tipo es requerido")
    @Schema(description = "Tipo de vehículo", example = "SEDAN, SUV, CAMIONETA, AUTOMOVIL")
    private TipoVehiculoEnum tipo;

    @NotNull(message = "El combustible es requerido")
    @Schema(description = "Tipo de combustible", example = "GASOLINA, DIESEL, ELECTRICO, HIBRIDO")
    private CombustibleVehiculoEnum combustible;

    @NotNull(message = "La condición es requerida")
    @Schema(description = "Condición del vehículo", example = "NUEVO, USADO")
    private CondicionVehiculoEnum condicion;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private String placa;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private IdentificadorVehiculoDTO identificadorVehiculo;
    public void setIdentificadorVehiculo(IdentificadorVehiculoDTO identificadorVehiculo) {
        this.identificadorVehiculo = identificadorVehiculo;
    }
    public IdentificadorVehiculoDTO getIdentificadorVehiculo() {
        return this.identificadorVehiculo;
    }
}
