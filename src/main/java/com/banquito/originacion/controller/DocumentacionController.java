package com.banquito.originacion.controller;

import com.banquito.originacion.controller.dto.DocumentoAdjuntoDTO;
import com.banquito.originacion.controller.dto.TipoDocumentoDTO;
import com.banquito.originacion.enums.CategoriaDocumentoEnum;
import com.banquito.originacion.service.DocumentacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1")
public class DocumentacionController {
    private final DocumentacionService documentacionService;

    public DocumentacionController(DocumentacionService documentacionService) {
        this.documentacionService = documentacionService;
    }

    @PostMapping("/documentos")
    public ResponseEntity<DocumentoAdjuntoDTO> cargarDocumento(@RequestParam Integer idSolicitud,
                                                              @RequestParam MultipartFile archivo,
                                                              @RequestParam Integer idTipoDocumento) {
        log.info("Cargando documento para solicitud: {}", idSolicitud);
        return ResponseEntity.ok(documentacionService.cargarDocumento(idSolicitud, archivo, idTipoDocumento));
    }

    @PostMapping("/documentos/firmados")
    public ResponseEntity<Void> registrarDocumentosFirmados(@RequestParam Integer idSolicitud,
                                                            @RequestParam List<MultipartFile> documentos) {
        log.info("Registrando documentos firmados para solicitud: {}", idSolicitud);
        documentacionService.registrarDocumentosFirmados(idSolicitud, documentos);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/documentos/solicitud/{idSolicitud}")
    public ResponseEntity<List<DocumentoAdjuntoDTO>> listarDocumentosPorSolicitud(@PathVariable Integer idSolicitud) {
        log.info("Listando documentos para solicitud: {}", idSolicitud);
        return ResponseEntity.ok(documentacionService.listarDocumentosPorSolicitud(idSolicitud));
    }

    @GetMapping("/documentos/{idDocumento}")
    public ResponseEntity<DocumentoAdjuntoDTO> obtenerDocumento(@PathVariable Integer idDocumento) {
        log.info("Obteniendo documento: {}", idDocumento);
        return ResponseEntity.ok(documentacionService.obtenerDocumento(idDocumento));
    }

    @DeleteMapping("/documentos/{idDocumento}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Integer idDocumento) {
        log.info("Eliminando documento: {}", idDocumento);
        documentacionService.eliminarDocumento(idDocumento);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tipos-documentos")
    public ResponseEntity<TipoDocumentoDTO> configurarTipoDocumento(@RequestBody TipoDocumentoDTO dto) {
        log.info("Configurando tipo de documento: {}", dto);
        return ResponseEntity.ok(documentacionService.configurarTipoDocumento(dto));
    }

    @GetMapping("/tipos-documentos/obligatorios")
    public ResponseEntity<List<TipoDocumentoDTO>> obtenerTiposObligatorios() {
        log.info("Obteniendo tipos de documentos obligatorios");
        return ResponseEntity.ok(documentacionService.obtenerTiposObligatorios());
    }

    @PatchMapping("/tipos-documentos/{id}/activar")
    public ResponseEntity<Void> activarTipoDocumento(@PathVariable Integer id) {
        log.info("Activando tipo de documento: {}", id);
        documentacionService.activarDesactivarTipo(id, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/tipos-documentos/{id}/desactivar")
    public ResponseEntity<Void> desactivarTipoDocumento(@PathVariable Integer id) {
        log.info("Desactivando tipo de documento: {}", id);
        documentacionService.activarDesactivarTipo(id, false);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/documentos/solicitud/{idSolicitud}/completitud")
    public ResponseEntity<Map<String, Object>> verificarCompletitud(@PathVariable Integer idSolicitud) {
        log.info("Verificando completitud documental para solicitud: {}", idSolicitud);
        return ResponseEntity.ok(documentacionService.verificarCompletitudDocumental(idSolicitud));
    }

    @GetMapping("/tipos-documentos/categoria/{categoria}")
    public ResponseEntity<List<TipoDocumentoDTO>> obtenerTiposPorCategoria(@PathVariable CategoriaDocumentoEnum categoria) {
        log.info("Obteniendo tipos de documentos por categoría: {}", categoria);
        return ResponseEntity.ok(documentacionService.obtenerTiposPorCategoria(categoria));
    }

    @GetMapping("/tipos-documentos")
    public ResponseEntity<List<TipoDocumentoDTO>> listarTodosTiposDocumento() {
        return ResponseEntity.ok(documentacionService.listarTodosTiposDocumento());
    }

    @GetMapping("/documentos")
    public ResponseEntity<List<DocumentoAdjuntoDTO>> listarTodosDocumentos() {
        return ResponseEntity.ok(documentacionService.listarTodosDocumentos());
    }
} 