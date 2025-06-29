package com.banquito.originacion.service;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.controller.mapper.AuditoriaMapper;
import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.exception.CreateEntityException;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.model.Auditoria;
import com.banquito.originacion.repository.AuditoriaRepository;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class AuditoriaService {

    private final AuditoriaRepository repository;
    private final AuditoriaMapper mapper;

    public AuditoriaService(AuditoriaRepository repository,
            AuditoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Lista todas las auditorías.
     */
    @Transactional(readOnly = true)
    public List<AuditoriaDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una auditoría por su ID.
     * Lanza ResourceNotFoundException si no existe.
     */
    @Transactional(readOnly = true)
    public AuditoriaDTO findById(Integer id) {
        Auditoria a = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Auditoría no encontrada con id=" + id));
        return mapper.toDTO(a);
    }

    /**
     * Lista todas las auditorías de una tabla (buscar por nombre parcial).
     */
    @Transactional(readOnly = true)
    public List<AuditoriaDTO> findByTabla(String tabla) {
        return repository.findByTablaContainingIgnoreCase(tabla).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las auditorías de un tipo de acción (INSERT, UPDATE o DELETE).
     */
    @Transactional(readOnly = true)
    public List<AuditoriaDTO> findByAccion(AccionAuditoriaEnum accion) {
        return repository.findByAccion(accion).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista las auditorías entre dos fechas (inclusive).
     */
    @Transactional(readOnly = true)
    public List<AuditoriaDTO> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta) {
        return repository.findByFechaHoraBetween(desde, hasta).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva entrada de auditoría.
     */

    @Transactional
    public AuditoriaDTO createAuditoria(@Valid AuditoriaDTO dto) {
        try {
            // Mapeo DTO → entidad
            Auditoria entity = mapper.toModel(dto);
            entity.setId(null);
            // Si no vino fechaHora, le ponemos ahora
            if (entity.getFechaHora() == null) {
                entity.setFechaHora(LocalDateTime.now());
            }
            // Guardamos
            Auditoria saved = repository.save(entity);
            return mapper.toDTO(saved);

        } catch (Exception e) {
            throw new CreateEntityException(
                    "Auditoria",
                    "Error al crear la entrada de auditoría. Detalle: " + e.getMessage());
        }
    }
}
