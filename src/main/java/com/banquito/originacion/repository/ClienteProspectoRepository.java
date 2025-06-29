package com.banquito.originacion.repository;

import com.banquito.originacion.enums.EstadoClientesEnum;
import com.banquito.originacion.model.ClienteProspecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteProspectoRepository extends JpaRepository<ClienteProspecto, Integer> {

    /**
     * Busca un cliente por su cédula (único)
     */
    Optional<ClienteProspecto> findByCedula(String cedula);

    /**
     * Busca clientes por su estado
     */
    List<ClienteProspecto> findByEstado(EstadoClientesEnum estado);

    /**
     * Busca clientes por nombre o apellido (búsqueda parcial, case-insensitive)
     */
    List<ClienteProspecto> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

    /**
     * Busca clientes con ingresos mayores o iguales al valor especificado
     */
    List<ClienteProspecto> findByIngresosGreaterThanEqual(BigDecimal ingresos);

    /**
     * Busca clientes donde los ingresos son mayores que los egresos
     * (La capacidad de endeudamiento se calculará en el servicio)
     */
    List<ClienteProspecto> findByIngresosGreaterThan(BigDecimal egresos);
    
    /**
     * Busca por actividad económica (búsqueda parcial, case-insensitive)
     */
    List<ClienteProspecto> findByActividadEconomicaContainingIgnoreCase(String actividad);

    /**
     * Busca clientes por email
     */
    Optional<ClienteProspecto> findByEmail(String email);

    /**
     * Busca clientes por teléfono
     */
    Optional<ClienteProspecto> findByTelefono(String telefono);

    /**
     * Verifica si existe un cliente con la cédula dada
     */
    boolean existsByCedula(String cedula);

    /**
     * Verifica si existe un cliente con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Busca clientes con ingresos mayores a los egresos por un factor específico
     * (útil para identificar clientes con solvencia económica)
     */
    List<ClienteProspecto> findByIngresosGreaterThanEqualAndEgresosLessThanEqual(
            BigDecimal ingresoMinimo, BigDecimal egresoMaximo);

    /**
     * Busca clientes por dirección (búsqueda parcial, case-insensitive)
     */
    List<ClienteProspecto> findByDireccionContainingIgnoreCase(String direccion);
} 