package com.banquito.gestion_vehiculos.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO para los identificadores únicos del vehículo")
public class IdentificadorVehiculoDTO {
    @Schema(description = "Placa del vehículo", example = "ABC-1234")
    @Size(max = 7, message = "La placa no puede exceder 7 caracteres")
    private String placa;
    @Schema(description = "Número de chasis", example = "1HGCM82633A004352")
    @Size(min = 17, max = 17, message = "El chasis debe tener exactamente 17 caracteres")
    private String chasis;
    @Schema(description = "Número de motor", example = "MTR123456789")
    @Size(max = 20, message = "El número de motor no puede exceder 20 caracteres")
    private String motor;
    @Schema(description = "ID generado automáticamente", accessMode = Schema.AccessMode.READ_ONLY, example = "6863125741aecf5c57b57ed0")
    private String id;
}


