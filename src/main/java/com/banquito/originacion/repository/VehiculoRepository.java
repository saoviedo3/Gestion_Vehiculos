package com.banquito.originacion.repository;

import com.banquito.originacion.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    // Buscar todos los vehículos de un concesionario
    List<Vehiculo> findByIdConcesionario(Integer idConcesionario);

    // Buscar por estado
    List<Vehiculo> findByEstado(com.banquito.originacion.enums.EstadoVehiculoEnum estado);

    // Buscar por marca (puede ser útil para filtros)
    List<Vehiculo> findByMarcaIgnoreCase(String marca);

    // Buscar por modelo (útil para filtros)
    List<Vehiculo> findByModeloIgnoreCase(String modelo);

    // Buscar por identificador único de vehículo (FK)
    Optional<Vehiculo> findByIdIdentificadorVehiculo(Integer idIdentificadorVehiculo);

    // Buscar todos los vehículos de un concesionario y estado específico
    List<Vehiculo> findByIdConcesionarioAndEstado(Integer idConcesionario, com.banquito.originacion.enums.EstadoVehiculoEnum estado);

    // Extra: Buscar por color si alguna vez quieres filtrar
    List<Vehiculo> findByColorIgnoreCase(String color);

    // Extra: Buscar por año específico
    List<Vehiculo> findByAnio(Integer anio);

    // Otros métodos CRUD ya los heredas de JpaRepository
}
