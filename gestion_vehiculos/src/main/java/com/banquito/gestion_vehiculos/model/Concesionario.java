package com.banquito.gestion_vehiculos.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;

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
@Document(collection = "concesionarios")
public class Concesionario {

    private String id;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String cuentaBancaria;
    private String emailContacto;
    private EstadoConcesionarioEnum estado;
    private List<Vendedor> vendedores;
    private List<Vehiculo> vehiculos;
    private Long version;

}
