package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {

    Optional<Vehiculo> findById(String id);

    boolean existsByMarca(String marca);

    boolean existsByModelo(String modelo);

    boolean existsByPlaca(String placa);

    Optional<Vehiculo> findByPlaca(String placa);

    List<Vehiculo> findByMarca(String marca);

    List<Vehiculo> findByModelo(String modelo);

    List<Vehiculo> findByIdConcesionario(String idConcesionario);

    List<Vehiculo> findByEstado(EstadoVehiculoEnum estado);

    Optional<Vehiculo> findByIdIdentificadorVehiculo(String idIdentificadorVehiculo);

}