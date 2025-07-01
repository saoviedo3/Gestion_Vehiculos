package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificadorVehiculoRepository extends MongoRepository<IdentificadorVehiculo, String> {
    IdentificadorVehiculo findByPlaca(String placa);
} 