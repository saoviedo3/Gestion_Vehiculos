package com.banquito.gestion_vehiculos.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document(collection = "identificadores_vehiculo")
public class IdentificadorVehiculo {
    @Id
    private String id;
    @org.springframework.data.mongodb.core.index.Indexed(name = "idxuIdentificadorVehiculo_placa", unique = true)
    private String placa;
    private String chasis;
    private String motor;
} 