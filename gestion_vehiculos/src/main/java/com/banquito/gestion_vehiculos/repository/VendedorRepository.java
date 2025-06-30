package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.model.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VendedorRepository extends MongoRepository<Vendedor, String> {

    List<Vendedor> findByIdConcesionario(String idConcesionario);

    List<Vendedor> findByEstado(String estado);

    Optional<Vendedor> findByEmail(String email);

    Optional<Vendedor> findByTelefono(String telefono);

    boolean existsByEmail(String email);

    boolean existsByTelefono(String telefono);

    List<Vendedor> findByIdConcesionarioAndEstado(String idConcesionario, String estado);
}
