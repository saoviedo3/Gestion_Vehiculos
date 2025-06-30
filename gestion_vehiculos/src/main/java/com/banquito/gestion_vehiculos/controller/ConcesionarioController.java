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

    @GetMapping("/ruc/{ruc}/vendedores")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByRuc(ruc));
    }

    @GetMapping("/ruc/{ruc}/vehiculos")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByRuc(ruc));
    }

    @PostMapping("/ruc/{ruc}/vendedores")
    public ResponseEntity<VendedorDTO> createVendedor(@PathVariable String ruc, @Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.createVendedorInConcesionario(ruc, dto));
    }

    @PutMapping("/ruc/{ruc}/vendedores/{idVendedor}")
    public ResponseEntity<VendedorDTO> updateVendedor(@PathVariable String ruc, @PathVariable String idVendedor, @Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVendedorInConcesionario(ruc, idVendedor, dto));
    }

    @PutMapping("/ruc/{ruc}/vendedores/{idVendedor}/desactivar")
    public ResponseEntity<VendedorDTO> desactivarVendedor(@PathVariable String ruc, @PathVariable String idVendedor) {
        return ResponseEntity.ok(concesionarioService.desactivarVendedorInConcesionario(ruc, idVendedor));
    }

    @PostMapping("/ruc/{ruc}/vehiculos")
    public ResponseEntity<VehiculoDTO> createVehiculo(@PathVariable String ruc, @Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(concesionarioService.createVehiculoInConcesionario(ruc, dto));
    }

    @PutMapping("/ruc/{ruc}/vehiculos/{idVehiculo}")
    public ResponseEntity<VehiculoDTO> updateVehiculo(@PathVariable String ruc, @PathVariable String idVehiculo, @Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVehiculoInConcesionario(ruc, idVehiculo, dto));
    }

    @PutMapping("/ruc/{ruc}/vehiculos/{idVehiculo}/desactivar")
    public ResponseEntity<VehiculoDTO> desactivarVehiculo(@PathVariable String ruc, @PathVariable String idVehiculo) {
        return ResponseEntity.ok(concesionarioService.desactivarVehiculoInConcesionario(ruc, idVehiculo));
    }

    @GetMapping("/ruc/{ruc}/vehiculos/placa/{placa}")
    public ResponseEntity<VehiculoDTO> getVehiculoByPlaca(@PathVariable String ruc, @PathVariable String placa) {
        return ResponseEntity.ok(concesionarioService.findVehiculoByPlacaInConcesionario(ruc, placa));
    }
} 