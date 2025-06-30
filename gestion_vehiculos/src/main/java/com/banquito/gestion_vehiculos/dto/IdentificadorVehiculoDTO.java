package com.banquito.gestion_vehiculos.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class IdentificadorVehiculoDTO {
    private String placa;
    private String chasis;
    private String motor;
} 