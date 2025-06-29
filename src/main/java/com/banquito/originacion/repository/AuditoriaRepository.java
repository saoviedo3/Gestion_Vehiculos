package com.banquito.originacion.repository;

import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

    /**
     * Devuelve todas las auditorías sobre una tabla concreta (búsqueda parcial, case-insensitive).
     */
    List<Auditoria> findByTablaContainingIgnoreCase(String tabla);

    /**
     * Devuelve todas las auditorías de una acción concreta (INSERT, UPDATE, DELETE).
     */
    List<Auditoria> findByAccion(AccionAuditoriaEnum accion);

    /**
     * Devuelve las auditorías cuyo timestamp está entre las dos fechas indicadas.
     */
    List<Auditoria> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
