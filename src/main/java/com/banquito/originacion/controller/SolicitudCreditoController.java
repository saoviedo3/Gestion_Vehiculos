package com.banquito.originacion.controller;

import com.banquito.originacion.controller.dto.SolicitudCreditoDTO;
import com.banquito.originacion.enums.EstadoSolicitudEnum;
import com.banquito.originacion.service.SolicitudCreditoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/solicitudes-credito")
public class SolicitudCreditoController {
    private final SolicitudCreditoService solicitudService;

    public SolicitudCreditoController(SolicitudCreditoService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudCreditoDTO> crearSolicitud(@RequestBody SolicitudCreditoDTO dto) {
        log.info("Creando solicitud de crédito: {}", dto);
        return ResponseEntity.ok(solicitudService.crearSolicitud(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCreditoDTO> actualizarSolicitud(@PathVariable Integer id, @RequestBody SolicitudCreditoDTO dto) {
        log.info("Actualizando solicitud de crédito id: {}", id);
        return ResponseEntity.ok(solicitudService.actualizarSolicitud(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestParam EstadoSolicitudEnum nuevoEstado, @RequestParam String motivo, @RequestParam String usuario) {
        log.info("Cambiando estado de solicitud id: {} a {}", id, nuevoEstado);
        solicitudService.cambiarEstado(id, nuevoEstado, motivo, usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/simular")
    public ResponseEntity<Map<String, Object>> simularCredito(@RequestParam Integer idVehiculo, @RequestParam Integer idClienteProspecto, @RequestParam Integer plazoMaximo, @RequestParam(required = false) BigDecimal scoreExterno) {
        log.info("Simulando crédito para vehículo {} y cliente {}", idVehiculo, idClienteProspecto);
        if (scoreExterno != null) {
            return ResponseEntity.ok(solicitudService.simularCredito(idVehiculo, idClienteProspecto, plazoMaximo, scoreExterno));
        } else {
            return ResponseEntity.ok(solicitudService.simularCredito(idVehiculo, idClienteProspecto, plazoMaximo));
        }
    }

    @PostMapping("/{id}/evaluar")
    public ResponseEntity<Map<String, Object>> evaluarCreditoAutomatico(@PathVariable Integer id) {
        log.info("Evaluando automáticamente solicitud id: {}", id);
        return ResponseEntity.ok(solicitudService.evaluarCreditoAutomatico(id));
    }

    @PostMapping("/{id}/instrumentar")
    public ResponseEntity<Void> instrumentarCredito(@PathVariable Integer id) {
        log.info("Instrumentando crédito para solicitud id: {}", id);
        solicitudService.instrumentarCredito(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/numero/{numeroSolicitud}")
    public ResponseEntity<SolicitudCreditoDTO> buscarPorNumero(@PathVariable String numeroSolicitud) {
        log.info("Buscando solicitud por número: {}", numeroSolicitud);
        return ResponseEntity.ok(solicitudService.buscarPorNumero(numeroSolicitud));
    }

    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorCliente(@PathVariable String cedula) {
        log.info("Listando solicitudes para cliente: {}", cedula);
        return ResponseEntity.ok(solicitudService.listarPorCliente(cedula));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorEstado(@PathVariable EstadoSolicitudEnum estado) {
        log.info("Listando solicitudes en estado: {}", estado);
        return ResponseEntity.ok(solicitudService.listarPorEstado(estado));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudCreditoDTO>> listarTodas() {
        log.info("Listando todas las solicitudes de crédito");
        return ResponseEntity.ok(solicitudService.listarTodas());
    }
} 