package com.banquito.originacion.repository;

import com.banquito.originacion.enums.EstadoSolicitudEnum;
import com.banquito.originacion.model.SolicitudCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudCreditoRepository extends JpaRepository<SolicitudCredito, Integer> {

    /**
     * Busca una solicitud por su número único
     */
    Optional<SolicitudCredito> findByNumeroSolicitud(String numeroSolicitud);

    /**
     * Busca todas las solicitudes de un cliente específico
     */
    List<SolicitudCredito> findByIdClienteProspecto(Integer idClienteProspecto);

    /**
     * Busca todas las solicitudes de un cliente por su cédula
     */
    List<SolicitudCredito> findByClienteProspectoCedula(String cedula);

    /**
     * Busca todas las solicitudes en un estado específico
     */
    List<SolicitudCredito> findByEstado(EstadoSolicitudEnum estado);

    /**
     * Busca solicitudes creadas entre un rango de fechas
     */
    List<SolicitudCredito> findByFechaSolicitudBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Busca solicitudes por montos dentro de un rango
     */
    List<SolicitudCredito> findByMontoSolicitadoBetween(BigDecimal montoMinimo, BigDecimal montoMaximo);

    /**
     * Busca solicitudes asociadas a un vehículo específico
     */
    List<SolicitudCredito> findByIdVehiculo(Integer idVehiculo);

    /**
     * Busca solicitudes con una relación cuota-ingreso menor o igual que el valor dado
     */
    List<SolicitudCredito> findByRelacionCuotaIngresoLessThanEqual(BigDecimal relacionMaxima);

    /**
     * Busca solicitudes por estado y ordenadas por fecha de solicitud descendente
     */
    List<SolicitudCredito> findByEstadoOrderByFechaSolicitudDesc(EstadoSolicitudEnum estado);

    /**
     * Verifica si existe una solicitud con el número dado
     */
    boolean existsByNumeroSolicitud(String numeroSolicitud);

    /**
     * Busca solicitudes por vendedor
     */
    List<SolicitudCredito> findByIdVendedor(Integer idVendedor);
} 