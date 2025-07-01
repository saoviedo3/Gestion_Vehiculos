package com.banquito.gestion_vehiculos.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO para los identificadores únicos del vehículo")
public class IdentificadorVehiculoDTO {
    @Schema(description = "Placa del vehículo", example = "ABC-1234")
    private String placa;
    @Schema(description = "Número de chasis", example = "1HGCM82633A004352")
    private String chasis;
    @Schema(description = "Número de motor", example = "MTR123456789")
    private String motor;
    @Schema(description = "ID generado automáticamente", accessMode = Schema.AccessMode.READ_ONLY, example = "6863125741aecf5c57b57ed0")
    private String id;
} 