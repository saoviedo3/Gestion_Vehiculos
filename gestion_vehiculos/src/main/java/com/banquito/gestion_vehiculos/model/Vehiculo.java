package com.banquito.gestion_vehiculos.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Document(collection = "vehiculos")
public class Vehiculo {

    private String id;
    private String marca;
    private String modelo;
    private double cilindraje;
    private int anio;
    private BigDecimal valor;
    private String color;
    private String extras;
    private String estado;
    private String tipo;
    private String combustible;
    private IdentificadorVehiculo identificadorVehiculo;
    private Long version;

}
