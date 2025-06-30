package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.model.Vehiculo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {

    Optional<Vehiculo> findById(String id);

    boolean existsByMarca(String marca);

    boolean existsByModelo(String modelo);

    boolean existsByPlaca(String placa);

    Optional<Vehiculo> findByMarca(String marca);

    Optional<Vehiculo> findByModelo(String modelo);

    Optional<Vehiculo> findByPlaca(String placa);
}