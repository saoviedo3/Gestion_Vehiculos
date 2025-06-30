package com.banquito.gestion_vehiculos.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class IdentificadorVehiculo {
    private String placa;
    private String chasis;
    private String motor;
} 