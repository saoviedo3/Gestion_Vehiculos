package com.banquito.gestion_vehiculos.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.banquito.gestion_vehiculos.enums.RolEnum;
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
@Document(collection = "usuarios")
public class Usuario {

    private String id;
    
    @Indexed(name = "idxuUsuario_email", unique = true)
    private String email;
    
    private String password; // Será hasheada con BCrypt
    
    private RolEnum rol;
    
    private String vendedorId; // nullable, solo para vendedores
    
    private String concesionarioId; // nullable, solo para vendedores
    
    private boolean activo = true;
    
    private Long version;
} 