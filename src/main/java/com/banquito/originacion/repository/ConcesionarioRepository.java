package com.banquito.originacion.repository;

import com.banquito.originacion.enums.EstadoConcesionarioEnum;
import com.banquito.originacion.model.Concesionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcesionarioRepository extends JpaRepository<Concesionario, Integer> {

    /**
     * Busca concesionarios por su estado (ACTIVO / INACTIVO).
     */
    List<Concesionario> findByEstado(EstadoConcesionarioEnum estado);

    /**
     * Busca un concesionario por su correo de contacto.
     */
    Optional<Concesionario> findByEmailContacto(String emailContacto);

    /**
     * Verifica si ya existe un concesionario con el email de contacto dado.
     */
    boolean existsByEmailContacto(String emailContacto);

    /**
     * Busca un concesionario por su teléfono.
     */
    Optional<Concesionario> findByTelefono(String telefono);

    /**
     * Verifica si ya existe un concesionario con el teléfono dado.
     */
    boolean existsByTelefono(String telefono);

    /**
     * Permite buscar concesionarios cuyo nombre (razón social) contenga la cadena especificada.
     */
    List<Concesionario> findByRazonSocialContainingIgnoreCase(String razonSocial);
}
