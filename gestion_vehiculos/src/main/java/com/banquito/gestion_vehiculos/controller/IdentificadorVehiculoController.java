package com.banquito.gestion_vehiculos.controller;

import com.banquito.gestion_vehiculos.dto.IdentificadorVehiculoDTO;
import com.banquito.gestion_vehiculos.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identificadores-vehiculo")
public class IdentificadorVehiculoController {

    private final VehiculoService vehiculoService;

    public IdentificadorVehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentificadorVehiculoDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(vehiculoService.getIdentificadorById(id));
    }

    @PostMapping
    public ResponseEntity<IdentificadorVehiculoDTO> create(@Valid @RequestBody IdentificadorVehiculoDTO dto) {
        return ResponseEntity.ok(vehiculoService.createIdentificador(dto));
    }
} 