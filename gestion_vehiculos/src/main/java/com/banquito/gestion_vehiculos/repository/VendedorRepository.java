package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum;
import com.banquito.gestion_vehiculos.model.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VendedorRepository extends MongoRepository<Vendedor, String> {

    Optional<Vendedor> findById(String id);

    List<Vendedor> findByEstado(EstadoVendedorEnum estado);

    Optional<Vendedor> findByEmail(String email);

    Optional<Vendedor> findByTelefono(String telefono);

    boolean existsByEmail(String email);

    boolean existsByTelefono(String telefono);
}
