package com.banquito.gestion_vehiculos.model;

import org.springframework.data.mongodb.core.index.Indexed;
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
@Document(collection = "identificadores_vehiculos")
public class IdentificadorVehiculo {

    private String id;
    @Indexed(name = "idxuIdentificador_vehiculo_vin", unique = true)
    private String vin;
    @Indexed(name = "idxuIdentificador_vehiculo_numeroMotor", unique = true)
    private String numeroMotor;
    @Indexed(name = "idxuIdentificador_vehiculo_placa", unique = true)
    private String placa;
    private Long version;

}
