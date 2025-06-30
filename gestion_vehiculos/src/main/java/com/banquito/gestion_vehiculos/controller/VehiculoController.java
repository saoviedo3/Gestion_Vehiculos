package com.banquito.gestion_vehiculos.controller;

import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<VehiculoDTO> getByPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.findVehiculoByPlaca(placa));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> getByEstado(@PathVariable EstadoVehiculoEnum estado) {
        return ResponseEntity.ok(vehiculoService.findVehiculosByEstado(estado));
    }

    @GetMapping("/concesionario/{idConcesionario}")
    public ResponseEntity<List<VehiculoDTO>> getByConcesionario(@PathVariable String idConcesionario) {
        return ResponseEntity.ok(vehiculoService.findVehiculosByConcesionario(idConcesionario));
    }

    @PostMapping
    public ResponseEntity<VehiculoDTO> create(@Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(vehiculoService.createVehiculo(dto));
    }

    @PutMapping("/placa/{placa}")
    public ResponseEntity<VehiculoDTO> update(@PathVariable String placa, @Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(vehiculoService.updateVehiculo(placa, dto));
    }
} 