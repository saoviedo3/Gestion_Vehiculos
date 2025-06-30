package com.banquito.gestion_vehiculos.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum;

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
@Document(collection = "vendedores")
public class Vendedor {

    private String id;
    private String nombre;
    private String telefono;
    @Indexed(name = "idxuVendedor_email", unique = true)
    private String email;
    private EstadoVendedorEnum estado;
    private Long version;

}
