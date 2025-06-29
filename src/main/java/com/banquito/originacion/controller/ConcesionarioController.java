package com.banquito.originacion.controller;

import com.banquito.originacion.controller.dto.ConcesionarioDTO;
import com.banquito.originacion.controller.dto.VendedorDTO;
import com.banquito.originacion.enums.EstadoConcesionarioEnum;
import com.banquito.originacion.enums.EstadoVendedorEnum;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.service.ConcesionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1")
@Tag(name = "Concesionarios y Vendedores", description = "Operaciones sobre concesionarios y vendedores")
public class ConcesionarioController {
    private final ConcesionarioService concesionarioService;

    public ConcesionarioController(ConcesionarioService concesionarioService) {
        this.concesionarioService = concesionarioService;
    }

    // Concesionario endpoints
    @GetMapping("/concesionarios")
    @Operation(summary = "Obtener todos los concesionarios")
    public ResponseEntity<List<ConcesionarioDTO>> getAllConcesionarios() {
        log.info("Obteniendo todos los concesionarios");
        return ResponseEntity.ok(concesionarioService.findAllConcesionarios());
    }

    @GetMapping("/concesionarios/{id}")
    @Operation(summary = "Obtener concesionario por ID")
    public ResponseEntity<ConcesionarioDTO> getConcesionarioById(@PathVariable Integer id) {
        log.info("Obteniendo concesionario por id: {}", id);
        return ResponseEntity.ok(concesionarioService.findConcesionarioById(id));
    }

    @GetMapping("/concesionarios/estado/{estado}")
    @Operation(summary = "Obtener concesionarios por estado")
    public ResponseEntity<List<ConcesionarioDTO>> getConcesionariosByEstado(@PathVariable EstadoConcesionarioEnum estado) {
        log.info("Obteniendo concesionarios por estado: {}", estado);
        return ResponseEntity.ok(concesionarioService.findConcesionariosByEstado(estado));
    }

    @GetMapping("/concesionarios/razon-social/{razon}")
    @Operation(summary = "Obtener concesionarios por razón social")
    public ResponseEntity<List<ConcesionarioDTO>> getConcesionariosByRazonSocial(@PathVariable String razon) {
        log.info("Obteniendo concesionarios por razón social: {}", razon);
        return ResponseEntity.ok(concesionarioService.findConcesionariosByRazonSocial(razon));
    }

    @GetMapping("/concesionarios/email/{email}")
    @Operation(summary = "Obtener concesionario por email de contacto")
    public ResponseEntity<ConcesionarioDTO> getConcesionarioByEmail(@PathVariable String email) {
        log.info("Obteniendo concesionario por email: {}", email);
        return ResponseEntity.ok(concesionarioService.findConcesionarioByEmail(email));
    }

    @PostMapping("/concesionarios")
    @Operation(summary = "Crear un nuevo concesionario")
    public ResponseEntity<ConcesionarioDTO> createConcesionario(@Valid @RequestBody ConcesionarioDTO dto) {
        log.info("Creando concesionario: {}", dto);
        return ResponseEntity.ok(concesionarioService.createConcesionario(dto));
    }

    @PutMapping("/concesionarios/{id}")
    @Operation(summary = "Actualizar un concesionario existente")
    public ResponseEntity<ConcesionarioDTO> updateConcesionario(@PathVariable Integer id, @Valid @RequestBody ConcesionarioDTO dto) {
        log.info("Actualizando concesionario id: {} con datos: {}", id, dto);
        return ResponseEntity.ok(concesionarioService.updateConcesionario(id, dto));
    }

    @PatchMapping("/concesionarios/{id}/desactivar")
    @Operation(summary = "Desactivar un concesionario")
    public ResponseEntity<ConcesionarioDTO> deactivateConcesionario(@PathVariable Integer id) {
        log.info("Desactivando concesionario id: {}", id);
        return ResponseEntity.ok(concesionarioService.deactivateConcesionario(id));
    }

    // Vendedor endpoints
    @GetMapping("/vendedores")
    @Operation(summary = "Obtener todos los vendedores")
    public ResponseEntity<List<VendedorDTO>> getAllVendedores() {
        log.info("Obteniendo todos los vendedores");
        return ResponseEntity.ok(concesionarioService.findAllVendedores());
    }

    @GetMapping("/vendedores/{id}")
    @Operation(summary = "Obtener vendedor por ID")
    public ResponseEntity<VendedorDTO> getVendedorById(@PathVariable Integer id) {
        log.info("Obteniendo vendedor por id: {}", id);
        return ResponseEntity.ok(concesionarioService.findVendedorById(id));
    }

    @GetMapping("/vendedores/email/{email}")
    @Operation(summary = "Obtener vendedor por email")
    public ResponseEntity<VendedorDTO> getVendedorByEmail(@PathVariable String email) {
        log.info("Obteniendo vendedor por email: {}", email);
        return ResponseEntity.ok(concesionarioService.findVendedorByEmail(email));
    }

    @GetMapping("/vendedores/concesionario/{idConcesionario}")
    @Operation(summary = "Obtener vendedores por concesionario")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByConcesionario(@PathVariable Integer idConcesionario) {
        log.info("Obteniendo vendedores por concesionario: {}", idConcesionario);
        return ResponseEntity.ok(concesionarioService.findVendedoresByConcesionario(idConcesionario));
    }

    @GetMapping("/vendedores/estado/{estado}")
    @Operation(summary = "Obtener vendedores por estado")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByEstado(@PathVariable EstadoVendedorEnum estado) {
        log.info("Obteniendo vendedores por estado: {}", estado);
        return ResponseEntity.ok(concesionarioService.findVendedoresByEstado(estado));
    }

    @PostMapping("/vendedores")
    @Operation(summary = "Crear un nuevo vendedor")
    public ResponseEntity<VendedorDTO> createVendedor(@Valid @RequestBody VendedorDTO dto) {
        log.info("Creando vendedor: {}", dto);
        return ResponseEntity.ok(concesionarioService.createVendedor(dto));
    }

    @PutMapping("/vendedores/{id}")
    @Operation(summary = "Actualizar un vendedor existente")
    public ResponseEntity<VendedorDTO> updateVendedor(@PathVariable Integer id, @Valid @RequestBody VendedorDTO dto) {
        log.info("Actualizando vendedor id: {} con datos: {}", id, dto);
        return ResponseEntity.ok(concesionarioService.updateVendedor(id, dto));
    }

    @PatchMapping("/vendedores/{id}/desactivar")
    @Operation(summary = "Desactivar un vendedor")
    public ResponseEntity<VendedorDTO> deactivateVendedor(@PathVariable Integer id) {
        log.info("Desactivando vendedor id: {}", id);
        return ResponseEntity.ok(concesionarioService.deactivateVendedor(id));
    }

    // Manejo de excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }
} 