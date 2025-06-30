package com.banquito.gestion_vehiculos.controller;

import com.banquito.gestion_vehiculos.dto.ConcesionarioDTO;
import com.banquito.gestion_vehiculos.dto.VendedorDTO;
import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;
import com.banquito.gestion_vehiculos.service.ConcesionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/concesionarios")
public class ConcesionarioController {

    private final ConcesionarioService concesionarioService;

    public ConcesionarioController(ConcesionarioService concesionarioService) {
        this.concesionarioService = concesionarioService;
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<ConcesionarioDTO> getByRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.findConcesionarioByRuc(ruc));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ConcesionarioDTO>> getByEstado(@PathVariable EstadoConcesionarioEnum estado) {
        return ResponseEntity.ok(concesionarioService.findConcesionariosByEstado(estado));
    }

    @GetMapping("/razon-social/{razonSocial}")
    public ResponseEntity<List<ConcesionarioDTO>> getByRazonSocial(@PathVariable String razonSocial) {
        return ResponseEntity.ok(concesionarioService.findConcesionariosByRazonSocial(razonSocial));
    }

    @GetMapping("/email/{emailContacto}")
    public ResponseEntity<ConcesionarioDTO> getByEmail(@PathVariable String emailContacto) {
        return ResponseEntity.ok(concesionarioService.findConcesionarioByEmail(emailContacto));
    }

    @PostMapping
    public ResponseEntity<ConcesionarioDTO> create(@Valid @RequestBody ConcesionarioDTO dto) {
        return ResponseEntity.ok(concesionarioService.createConcesionario(dto));
    }

    @PutMapping("/ruc/{ruc}")
    public ResponseEntity<ConcesionarioDTO> update(@PathVariable String ruc, @Valid @RequestBody ConcesionarioDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateConcesionario(ruc, dto));
    }

    @PutMapping("/ruc/{ruc}/desactivar")
    public ResponseEntity<ConcesionarioDTO> desactivar(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.desactivateConcesionario(ruc));
    }

    @GetMapping("/{idConcesionario}/vendedores")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByConcesionario(@PathVariable String idConcesionario) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByConcesionario(idConcesionario));
    }

    @GetMapping("/{idConcesionario}/vehiculos")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByConcesionario(@PathVariable String idConcesionario) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByConcesionario(idConcesionario));
    }
} 