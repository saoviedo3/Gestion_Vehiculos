package com.banquito.originacion.controller;

import com.banquito.originacion.controller.dto.ClienteProspectoDTO;
import com.banquito.originacion.service.ClienteProspectoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/clientes-prospectos")
public class ClienteProspectoController {
    private final ClienteProspectoService clienteService;

    public ClienteProspectoController(ClienteProspectoService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteProspectoDTO> registrarCliente(@Valid @RequestBody ClienteProspectoDTO dto) {
        log.info("Registrando cliente prospecto: {}", dto);
        return ResponseEntity.ok(clienteService.registrarCliente(dto));
    }

    @GetMapping("/validar/{cedula}")
    public ResponseEntity<Map<String, Object>> validarCliente(@PathVariable String cedula) {
        log.info("Validando cliente con cédula: {}", cedula);
        return ResponseEntity.ok(clienteService.validarCliente(cedula));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<ClienteProspectoDTO> obtenerPorCedula(@PathVariable String cedula) {
        log.info("Obteniendo cliente por cédula: {}", cedula);
        return ResponseEntity.ok(clienteService.obtenerPorCedula(cedula));
    }

    @PutMapping("/{id}/financiera")
    public ResponseEntity<ClienteProspectoDTO> actualizarFinanciera(@PathVariable Integer id, @RequestParam BigDecimal ingresos, @RequestParam BigDecimal egresos) {
        log.info("Actualizando información financiera para cliente id: {}", id);
        return ResponseEntity.ok(clienteService.actualizarInformacionFinanciera(id, ingresos, egresos));
    }

    @GetMapping("/{cedula}/clasificar")
    public ResponseEntity<Map<String, Object>> clasificarCliente(@PathVariable String cedula) {
        log.info("Clasificando cliente con cédula: {}", cedula);
        return ResponseEntity.ok(clienteService.clasificarCliente(cedula));
    }

    @GetMapping("/{cedula}/historial")
    public ResponseEntity<List<Map<String, Object>>> historialCliente(@PathVariable String cedula) {
        log.info("Obteniendo historial para cliente con cédula: {}", cedula);
        return ResponseEntity.ok(clienteService.obtenerHistorialCliente(cedula));
    }

    @GetMapping("/{cedula}/capacidad-financiera")
    public ResponseEntity<Map<String, Object>> validarCapacidadFinanciera(@PathVariable String cedula, @RequestParam BigDecimal cuotaProyectada) {
        log.info("Validando capacidad financiera para cliente: {}", cedula);
        return ResponseEntity.ok(clienteService.validarCapacidadFinanciera(cedula, cuotaProyectada));
    }

    @GetMapping
    public ResponseEntity<List<ClienteProspectoDTO>> obtenerTodos() {
        log.info("Obteniendo todos los clientes prospecto");
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }
} 