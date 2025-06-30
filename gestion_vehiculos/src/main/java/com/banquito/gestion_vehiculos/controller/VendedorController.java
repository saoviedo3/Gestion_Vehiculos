package com.banquito.gestion_vehiculos.controller;

import com.banquito.gestion_vehiculos.dto.VendedorDTO;
import com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum;
import com.banquito.gestion_vehiculos.service.ConcesionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final ConcesionarioService concesionarioService;

    public VendedorController(ConcesionarioService concesionarioService) {
        this.concesionarioService = concesionarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(concesionarioService.findVendedorById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<VendedorDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(concesionarioService.findVendedorByEmail(email));
    }

    @GetMapping("/concesionario/{idConcesionario}")
    public ResponseEntity<List<VendedorDTO>> getByConcesionario(@PathVariable String idConcesionario) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByConcesionario(idConcesionario));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VendedorDTO>> getByEstado(@PathVariable EstadoVendedorEnum estado) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<VendedorDTO> create(@Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.createVendedor(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorDTO> update(@PathVariable String id, @Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVendedor(id, dto));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<VendedorDTO> desactivar(@PathVariable String id) {
        return ResponseEntity.ok(concesionarioService.desactivateVendedor(id));
    }
} 