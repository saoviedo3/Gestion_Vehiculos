package com.banquito.originacion.controller;

import com.banquito.originacion.controller.dto.VehiculoDTO;
import com.banquito.originacion.controller.dto.IdentificadorVehiculoDTO;
import com.banquito.originacion.controller.mapper.VehiculoMapper;
import com.banquito.originacion.controller.mapper.IdentificadorVehiculoMapper;
import com.banquito.originacion.enums.EstadoVehiculoEnum;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.model.Vehiculo;
import com.banquito.originacion.service.VehiculoService;
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
@Tag(name = "Vehiculos", description = "Operaciones sobre vehículos y sus identificadores")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    // Vehiculo endpoints
    @GetMapping("/vehiculos")
    @Operation(summary = "Obtener todos los vehículos")
    public ResponseEntity<List<VehiculoDTO>> getAllVehiculos() {
        log.info("Obteniendo todos los vehículos");
        return ResponseEntity.ok(vehiculoService.findAllVehiculos());
    }

    @GetMapping("/vehiculos/{id}")
    @Operation(summary = "Obtener vehículo por ID")
    public ResponseEntity<VehiculoDTO> getVehiculoById(@PathVariable Integer id) {
        log.info("Obteniendo vehículo por id: {}", id);
        return ResponseEntity.ok(vehiculoService.findVehiculoById(id));
    }

    @GetMapping("/vehiculos/concesionario/{idConcesionario}")
    @Operation(summary = "Obtener vehículos por concesionario")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByConcesionario(@PathVariable Integer idConcesionario) {
        log.info("Obteniendo vehículos por concesionario: {}", idConcesionario);
        return ResponseEntity.ok(vehiculoService.findVehiculosByConcesionario(idConcesionario));
    }

    @GetMapping("/vehiculos/estado/{estado}")
    @Operation(summary = "Obtener vehículos por estado")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByEstado(@PathVariable EstadoVehiculoEnum estado) {
        log.info("Obteniendo vehículos por estado: {}", estado);
        return ResponseEntity.ok(vehiculoService.findVehiculosByEstado(estado));
    }

    @GetMapping("/vehiculos/marca/{marca}")
    @Operation(summary = "Obtener vehículos por marca")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByMarca(@PathVariable String marca) {
        log.info("Obteniendo vehículos por marca: {}", marca);
        return ResponseEntity.ok(vehiculoService.findVehiculosByMarca(marca));
    }

    @GetMapping("/vehiculos/modelo/{modelo}")
    @Operation(summary = "Obtener vehículos por modelo")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByModelo(@PathVariable String modelo) {
        log.info("Obteniendo vehículos por modelo: {}", modelo);
        return ResponseEntity.ok(vehiculoService.findVehiculosByModelo(modelo));
    }

    @GetMapping("/vehiculos/identificador/{idIdentificador}")
    @Operation(summary = "Obtener vehículo por identificador")
    public ResponseEntity<VehiculoDTO> getVehiculoByIdentificador(@PathVariable Integer idIdentificador) {
        log.info("Obteniendo vehículo por identificador: {}", idIdentificador);
        return ResponseEntity.ok(vehiculoService.findVehiculoByIdentificador(idIdentificador));
    }

    @PostMapping("/vehiculos")
    @Operation(summary = "Crear un nuevo vehículo")
    public ResponseEntity<VehiculoDTO> createVehiculo(@Valid @RequestBody VehiculoDTO dto) {
        log.info("Creando vehículo: {}", dto);
        return ResponseEntity.ok(vehiculoService.createVehiculo(dto));
    }

    @PutMapping("/vehiculos/{id}")
    @Operation(summary = "Actualizar un vehículo existente")
    public ResponseEntity<VehiculoDTO> updateVehiculo(@PathVariable Integer id, @Valid @RequestBody VehiculoDTO dto) {
        log.info("Actualizando vehículo id: {} con datos: {}", id, dto);
        return ResponseEntity.ok(vehiculoService.updateVehiculo(id, dto));
    }

    @DeleteMapping("/vehiculos/{id}")
    @Operation(summary = "Eliminar un vehículo por ID")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Integer id) {
        log.info("Eliminando vehículo id: {}", id);
        vehiculoService.deleteVehiculo(id);
        return ResponseEntity.noContent().build();
    }

    // IdentificadorVehiculo endpoints
    @GetMapping("/identificadores-vehiculos")
    @Operation(summary = "Obtener todos los identificadores de vehículos")
    public ResponseEntity<List<IdentificadorVehiculoDTO>> getAllIdentificadores() {
        log.info("Obteniendo todos los identificadores de vehículos");
        return ResponseEntity.ok(vehiculoService.getAllIdentificadores());
    }

    @GetMapping("/identificadores-vehiculos/{id}")
    @Operation(summary = "Obtener identificador de vehículo por ID")
    public ResponseEntity<IdentificadorVehiculoDTO> getIdentificadorById(@PathVariable Integer id) {
        log.info("Obteniendo identificador de vehículo por id: {}", id);
        return ResponseEntity.ok(vehiculoService.getIdentificadorById(id));
    }

    @PostMapping("/identificadores-vehiculos")
    @Operation(summary = "Crear un nuevo identificador de vehículo")
    public ResponseEntity<IdentificadorVehiculoDTO> createIdentificador(@Valid @RequestBody IdentificadorVehiculoDTO dto) {
        log.info("Creando identificador de vehículo: {}", dto);
        return ResponseEntity.ok(vehiculoService.createIdentificador(dto));
    }

    // Manejo de excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }
} 