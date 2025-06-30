package com.banquito.gestion_vehiculos.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;

public interface IdentificadorVehiculoRepository extends MongoRepository<IdentificadorVehiculo, String> {

    boolean existsByVin(String vin);

    boolean existsByNumeroMotor(String numeroMotor);

    boolean existsByPlaca(String placa);

    Optional<IdentificadorVehiculo> findByVin(String vin);

    Optional<IdentificadorVehiculo> findByNumeroMotor(String numeroMotor);

    Optional<IdentificadorVehiculo> findByPlaca(String placa);
}
