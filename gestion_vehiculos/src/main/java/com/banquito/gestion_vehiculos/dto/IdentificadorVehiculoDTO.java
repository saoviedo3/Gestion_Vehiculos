package com.banquito.gestion_vehiculos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de identificadores de vehículos")
public class IdentificadorVehiculoDTO {

    @Schema(description = "Identificador único del identificador de vehículo", example = "1")
    private String id;

    @NotBlank(message = "El VIN es requerido")
    @Size(min = 17, max = 17, message = "El VIN debe tener exactamente 17 caracteres")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "El VIN debe contener solo caracteres alfanuméricos válidos (excluyendo I, O, Q)")
    @Schema(description = "Número de identificación del vehículo (VIN)", example = "1HGBH41JXMN109186", minLength = 17, maxLength = 17)
    private String vin;

    @NotBlank(message = "El número de motor es requerido")
    @Size(max = 20, message = "El número de motor no puede exceder 20 caracteres")
    @Schema(description = "Número del motor del vehículo", example = "ABC123456789", maxLength = 20)
    private String numeroMotor;

    @NotBlank(message = "La placa es requerida")
    @Size(max = 7, message = "La placa no puede exceder 7 caracteres")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{3,4}$", message = "La placa debe tener el formato correcto (ej: ABC1234)")
    @Schema(description = "Placa del vehículo", example = "ABC1234", maxLength = 7)
    private String placa;

    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
}
