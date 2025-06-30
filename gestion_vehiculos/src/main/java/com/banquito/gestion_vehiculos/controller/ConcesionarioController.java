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

    @PutMapping("/ruc/{ruc}/vendedores/{cedula}")
    public ResponseEntity<VendedorDTO> updateVendedor(@PathVariable String ruc, @PathVariable String cedula, @Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVendedorInConcesionarioByCedula(ruc, cedula, dto));
    }

    @PutMapping("/ruc/{ruc}/vendedores/{cedula}/desactivar")
    public ResponseEntity<VendedorDTO> desactivarVendedor(@PathVariable String ruc, @PathVariable String cedula) {
        return ResponseEntity.ok(concesionarioService.desactivarVendedorInConcesionarioByCedula(ruc, cedula));
    }

    @GetMapping("/ruc/{ruc}/vendedores/cedula/{cedula}")
    public ResponseEntity<VendedorDTO> getVendedorByCedula(@PathVariable String ruc, @PathVariable String cedula) {
        return ResponseEntity.ok(concesionarioService.findVendedorByCedulaInConcesionario(ruc, cedula));
    }

    @GetMapping("/ruc/{ruc}/vendedores/estado/{estado}")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByEstado(@PathVariable String ruc, @PathVariable String estado) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByEstadoInConcesionario(ruc, estado));
    }

    @GetMapping("/ruc/{ruc}/vendedores/email/{email}")
    public ResponseEntity<VendedorDTO> getVendedorByEmail(@PathVariable String ruc, @PathVariable String email) {
        return ResponseEntity.ok(concesionarioService.findVendedorByEmailInConcesionario(ruc, email));
    }

    @PostMapping("/ruc/{ruc}/vehiculos")
    public ResponseEntity<VehiculoDTO> createVehiculo(@PathVariable String ruc, @Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(concesionarioService.createVehiculoInConcesionario(ruc, dto));
    }

    @PutMapping("/ruc/{ruc}/vehiculos/{placa}")
    public ResponseEntity<VehiculoDTO> updateVehiculo(@PathVariable String ruc, @PathVariable String placa, @Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVehiculoInConcesionarioByPlaca(ruc, placa, dto));
    }

    @PutMapping("/ruc/{ruc}/vehiculos/{placa}/desactivar")
    public ResponseEntity<VehiculoDTO> desactivarVehiculo(@PathVariable String ruc, @PathVariable String placa) {
        return ResponseEntity.ok(concesionarioService.desactivarVehiculoInConcesionarioByPlaca(ruc, placa));
    }

    @GetMapping("/ruc/{ruc}/vehiculos/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByEstado(@PathVariable String ruc, @PathVariable String estado) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByEstadoInConcesionario(ruc, estado));
    }

    @GetMapping("/ruc/{ruc}/vehiculos/condicion/{condicion}")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByCondicion(@PathVariable String ruc, @PathVariable String condicion) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByCondicionInConcesionario(ruc, condicion));
    }

    @GetMapping("/ruc/{ruc}/vehiculos/placa/{placa}")
    public ResponseEntity<VehiculoDTO> getVehiculoByPlaca(@PathVariable String ruc, @PathVariable String placa) {
        return ResponseEntity.ok(concesionarioService.findVehiculoByPlacaInConcesionario(ruc, placa));
    }
} 