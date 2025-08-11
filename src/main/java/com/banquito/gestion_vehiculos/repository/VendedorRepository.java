package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.model.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends MongoRepository<Vendedor, String> {
    List<Vendedor> findByNombreContainingIgnoreCase(String nombre);
    Optional<Vendedor> findByEmail(String email);
} 