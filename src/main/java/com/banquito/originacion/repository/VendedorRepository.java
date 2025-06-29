package com.banquito.originacion.repository;

import com.banquito.originacion.enums.EstadoVendedorEnum;
import com.banquito.originacion.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {

    /**
     * Devuelve todos los vendedores de un concesionario dado.
     */
    List<Vendedor> findByIdConcesionario(Integer idConcesionario);

    /**
     * Devuelve todos los vendedores en un estado concreto (ACTIVO / INACTIVO).
     */
    List<Vendedor> findByEstado(EstadoVendedorEnum estado);

    /**
     * Busca un vendedor por su email.
     */
    Optional<Vendedor> findByEmail(String email);

    /**
     * Busca un vendedor por su teléfono.
     */
    Optional<Vendedor> findByTelefono(String telefono);

    /**
     * Verifica existencia por email (útil para validaciones de unicidad).
     */
    boolean existsByEmail(String email);

    /**
     * Verifica existencia por teléfono (útil para validaciones de unicidad).
     */
    boolean existsByTelefono(String telefono);

    /**
     * Devuelve todos los vendedores de un concesionario y en un estado determinado.
     */
    List<Vendedor> findByIdConcesionarioAndEstado(Integer idConcesionario, EstadoVendedorEnum estado);
}
