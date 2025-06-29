package com.banquito.originacion.controller;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/auditorias")
@Tag(name = "Auditorias", description = "Operaciones sobre auditorías")
public class AuditoriaController {
    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las auditorías")
    public ResponseEntity<List<AuditoriaDTO>> getAll() {
        log.info("Obteniendo todas las auditorías");
        return ResponseEntity.ok(auditoriaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener auditoría por ID")
    public ResponseEntity<AuditoriaDTO> getById(@PathVariable Integer id) {
        log.info("Obteniendo auditoría por id: {}", id);
        return ResponseEntity.ok(auditoriaService.findById(id));
    }

    @GetMapping("/tabla/{tabla}")
    @Operation(summary = "Obtener auditorías por tabla")
    public ResponseEntity<List<AuditoriaDTO>> getByTabla(@PathVariable String tabla) {
        log.info("Obteniendo auditorías por tabla: {}", tabla);
        return ResponseEntity.ok(auditoriaService.findByTabla(tabla));
    }

    @GetMapping("/accion/{accion}")
    @Operation(summary = "Obtener auditorías por acción")
    public ResponseEntity<List<AuditoriaDTO>> getByAccion(@PathVariable AccionAuditoriaEnum accion) {
        log.info("Obteniendo auditorías por acción: {}", accion);
        return ResponseEntity.ok(auditoriaService.findByAccion(accion));
    }

    @GetMapping("/fechas")
    @Operation(summary = "Obtener auditorías por rango de fechas")
    public ResponseEntity<List<AuditoriaDTO>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        log.info("Obteniendo auditorías entre {} y {}", desde, hasta);
        return ResponseEntity.ok(auditoriaService.findByFechaHoraBetween(desde, hasta));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva auditoría")
    public ResponseEntity<AuditoriaDTO> create(@Valid @RequestBody AuditoriaDTO dto) {
        log.info("Creando auditoría: {}", dto);
        return ResponseEntity.ok(auditoriaService.createAuditoria(dto));
    }

    // Manejo de excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }
} 