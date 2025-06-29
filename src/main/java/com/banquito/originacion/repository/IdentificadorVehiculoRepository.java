package com.banquito.originacion.repository;

import com.banquito.originacion.model.IdentificadorVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentificadorVehiculoRepository
        extends JpaRepository<IdentificadorVehiculo, Integer> {

    boolean existsByVin(String vin);

    boolean existsByNumeroMotor(String numeroMotor);

    boolean existsByPlaca(String placa);

    Optional<IdentificadorVehiculo> findByVin(String vin);

    Optional<IdentificadorVehiculo> findByNumeroMotor(String numeroMotor);

    Optional<IdentificadorVehiculo> findByPlaca(String placa);
}
