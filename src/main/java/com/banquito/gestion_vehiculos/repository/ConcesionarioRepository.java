package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.model.Concesionario;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;

import java.util.List;
import java.util.Optional;

public interface ConcesionarioRepository extends MongoRepository<Concesionario, String> {

    List<Concesionario> findByEstado(EstadoConcesionarioEnum estado);

    Optional<Concesionario> findByEmailContacto(String emailContacto);

    boolean existsByEmailContacto(String emailContacto);

    Optional<Concesionario> findByTelefono(String telefono);

    boolean existsByTelefono(String telefono);

    List<Concesionario> findByRazonSocialContainingIgnoreCase(String razonSocial);

    Optional<Concesionario> findByRuc(String ruc);

    boolean existsByRuc(String ruc);
    
    // Buscar concesionario que contiene un vendedor específico
    Optional<Concesionario> findByVendedoresId(String vendedorId);
}