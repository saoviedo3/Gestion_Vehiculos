package com.banquito.gestion_vehiculos.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.TipoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CombustibleVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CondicionVehiculoEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;

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
    private String anio;
    private BigDecimal valor;
    private String color;
    private String extras;
    private EstadoVehiculoEnum estado;
    private TipoVehiculoEnum tipo;
    private CombustibleVehiculoEnum combustible;
    private IdentificadorVehiculo identificadorVehiculo;
    private Long version;
    private CondicionVehiculoEnum condicion;

}
