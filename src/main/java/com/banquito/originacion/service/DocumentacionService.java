package com.banquito.originacion.service;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.controller.dto.DocumentoAdjuntoDTO;
import com.banquito.originacion.controller.dto.TipoDocumentoDTO;
import com.banquito.originacion.controller.mapper.DocumentoAdjuntoMapper;
import com.banquito.originacion.controller.mapper.TipoDocumentoMapper;
import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.enums.CategoriaDocumentoEnum;
import com.banquito.originacion.enums.EstadoTiposDocumentoEnum;
import com.banquito.originacion.exception.CreateEntityException;
import com.banquito.originacion.exception.DeleteEntityException;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.exception.UpdateEntityException;
import com.banquito.originacion.model.DocumentoAdjunto;
import com.banquito.originacion.model.TipoDocumento;
import com.banquito.originacion.repository.DocumentoAdjuntoRepository;
import com.banquito.originacion.repository.TipoDocumentoRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class DocumentacionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentacionService.class);
    private static final String DOCUMENTOS_BASE_PATH = "documentos/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private final DocumentoAdjuntoRepository documentoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final DocumentoAdjuntoMapper documentoMapper;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final AuditoriaService auditoriaService;

    public DocumentacionService(DocumentoAdjuntoRepository documentoRepository,
                             TipoDocumentoRepository tipoDocumentoRepository,
                             DocumentoAdjuntoMapper documentoMapper,
                             TipoDocumentoMapper tipoDocumentoMapper,
                             AuditoriaService auditoriaService) {
        this.documentoRepository = documentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.documentoMapper = documentoMapper;
        this.tipoDocumentoMapper = tipoDocumentoMapper;
        this.auditoriaService = auditoriaService;
    }

    // === GESTIÓN DE DOCUMENTOS ===
    @Transactional
    public DocumentoAdjuntoDTO cargarDocumento(Integer idSolicitud, MultipartFile archivo, Integer idTipoDocumento) {
        try {
            // 1. Validar formato PDF únicamente
            if (!validarFormatoPDF(archivo)) {
                throw new CreateEntityException("Documento", "El archivo debe ser en formato PDF");
            }

            // 2. Validar tamaño máximo
            if (archivo.getSize() > MAX_FILE_SIZE) {
                throw new CreateEntityException("Documento", "El tamaño del archivo excede el límite permitido");
            }

            // 3. Validar integridad archivo
            if (!validarIntegridadArchivo(archivo)) {
                throw new CreateEntityException("Documento", "El archivo está corrupto o incompleto");
            }

            // Verificar que exista el tipo de documento
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento no encontrado con id=" + idTipoDocumento));

            // 4. Generar nombre único
            String nombreArchivo = generarNombreUnico(archivo.getOriginalFilename(), idSolicitud);
            
            // Construir ruta completa
            String rutaArchivo = guardarArchivo(archivo, nombreArchivo);

            // 5. Encriptar si es documento sensible
            if (tipoDocumento.getDescripcion().toLowerCase().contains("buró") ||
                tipoDocumento.getDescripcion().toLowerCase().contains("confidencial")) {
                encriptarDocumentoSensible(rutaArchivo, idTipoDocumento);
            }

            // 6. Asociar a solicitud
            DocumentoAdjuntoDTO documentoDTO = new DocumentoAdjuntoDTO();
            documentoDTO.setIdSolicitud(idSolicitud);
            documentoDTO.setIdTipoDocumento(idTipoDocumento);
            documentoDTO.setRutaArchivo(rutaArchivo);
            documentoDTO.setFechaCargado(LocalDateTime.now());
            
            // 7. Validar plazo carga documentos firmados
            if (tipoDocumento.getDescripcion().toLowerCase().contains("contrato") ||
                tipoDocumento.getDescripcion().toLowerCase().contains("pagaré")) {
                if (!validarPlazoCarga(idSolicitud, documentoDTO.getFechaCargado())) {
                    throw new CreateEntityException("Documento", 
                            "Ha superado el plazo máximo para cargar documentos firmados");
                }
            }
            
            // Guardar en la base de datos
            DocumentoAdjunto entity = documentoMapper.toModel(documentoDTO);
            entity = documentoRepository.save(entity);
            
            // Registrar auditoría
            registrarAuditoria("documentos_adjuntos", AccionAuditoriaEnum.INSERT);
            
            return documentoMapper.toDTO(entity);
        } catch (IOException e) {
            throw new CreateEntityException("Documento", "Error al guardar el documento: " + e.getMessage());
        }
    }

    @Transactional
    public void validarDocumento(Integer idDocumento) {
        try {
            DocumentoAdjunto documento = documentoRepository.findById(idDocumento)
                    .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con id=" + idDocumento));
            
            // Aquí se implementarían validaciones específicas post-carga
            // Por ejemplo, verificar que el documento es legible, completo, etc.
            
            // Para este ejemplo, simplemente registramos la validación
            log.info("Documento validado correctamente: {}", idDocumento);
            registrarAuditoria("documentos_adjuntos", AccionAuditoriaEnum.UPDATE);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Documento", "Error al validar el documento: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminarDocumento(Integer idDocumento) {
        try {
            DocumentoAdjunto documento = documentoRepository.findById(idDocumento)
                    .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con id=" + idDocumento));
            
            // Eliminación segura del archivo físico (si existe)
            try {
                Path archivoPath = Paths.get(documento.getRutaArchivo());
                if (Files.exists(archivoPath)) {
                    Files.delete(archivoPath);
                }
            } catch (IOException e) {
                log.warn("No se pudo eliminar el archivo físico: {}", documento.getRutaArchivo());
            }
            
            // Eliminar de la base de datos
            documentoRepository.delete(documento);
            
            // Registrar auditoría
            registrarAuditoria("documentos_adjuntos", AccionAuditoriaEnum.DELETE);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DeleteEntityException("Documento", "Error al eliminar el documento: " + e.getMessage());
        }
    }

    // === VERIFICACIÓN Y COMPLETITUD ===
    public Map<String, Object> verificarCompletitudDocumental(Integer idSolicitud) {
        // 1. Obtener tipos obligatorios para la etapa actual
        List<TipoDocumentoDTO> tiposObligatorios = obtenerTiposObligatorios();
        
        // 2. Verificar documentos cargados vs requeridos
        List<DocumentoAdjunto> documentosCargados = documentoRepository.findByIdSolicitud(idSolicitud);
        
        // Lista para almacenar IDs de tipos de documentos ya cargados
        List<Integer> idTiposDocumentosCargados = documentosCargados.stream()
                .map(DocumentoAdjunto::getIdTipoDocumento)
                .collect(Collectors.toList());
        
        // Lista para almacenar tipos de documentos faltantes
        List<TipoDocumentoDTO> documentosFaltantes = new ArrayList<>();
        
        for (TipoDocumentoDTO tipoObligatorio : tiposObligatorios) {
            if (!idTiposDocumentosCargados.contains(tipoObligatorio.getId())) {
                documentosFaltantes.add(tipoObligatorio);
            }
        }
        
        // 3. Generar reporte de completitud
        double porcentajeCompletitud = 0.0;
        if (!tiposObligatorios.isEmpty()) {
            int documentosObligatoriosCargados = tiposObligatorios.size() - documentosFaltantes.size();
            porcentajeCompletitud = (double) documentosObligatoriosCargados / tiposObligatorios.size() * 100.0;
        }
        
        // Crear mapa de respuesta
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("idSolicitud", idSolicitud);
        respuesta.put("totalDocumentosRequeridos", tiposObligatorios.size());
        respuesta.put("documentosCargados", documentosCargados.size());
        respuesta.put("documentosFaltantes", documentosFaltantes);
        respuesta.put("porcentajeCompletitud", porcentajeCompletitud);
        respuesta.put("completitudTotal", documentosFaltantes.isEmpty());
        
        return respuesta;
    }

    public List<TipoDocumentoDTO> obtenerDocumentosFaltantes(Integer idSolicitud) {
        // Obtener documentos obligatorios
        List<TipoDocumentoDTO> tiposObligatorios = obtenerTiposObligatorios();
        
        // Obtener documentos ya cargados para la solicitud
        List<DocumentoAdjunto> documentosCargados = documentoRepository.findByIdSolicitud(idSolicitud);
        List<Integer> idTiposDocumentosCargados = documentosCargados.stream()
                .map(DocumentoAdjunto::getIdTipoDocumento)
                .collect(Collectors.toList());
        
        // Filtrar los tipos obligatorios que no han sido cargados
        return tiposObligatorios.stream()
                .filter(tipo -> !idTiposDocumentosCargados.contains(tipo.getId()))
                .collect(Collectors.toList());
    }

    public void alertarDocumentosPendientes(Integer idSolicitud) {
        // 1. Verificar documentos faltantes
        List<TipoDocumentoDTO> documentosFaltantes = obtenerDocumentosFaltantes(idSolicitud);
        
        if (!documentosFaltantes.isEmpty()) {
            // 2. Generar alertas automáticas
            log.info("ALERTA: Documentos pendientes para solicitud {}: {}", 
                    idSolicitud, 
                    documentosFaltantes.stream()
                        .map(TipoDocumentoDTO::getNombre)
                        .collect(Collectors.joining(", ")));
            
            // 3. Notificar a involucrados
            // En un sistema real, aquí se enviarían notificaciones por correo, SMS, etc.
            
            // Registrar en la auditoría
            registrarAuditoria("solicitudes_credito", AccionAuditoriaEnum.UPDATE);
        }
    }

    // === GESTIÓN DE TIPOS DOCUMENTOS ===
    @Transactional
    public TipoDocumentoDTO configurarTipoDocumento(@Valid TipoDocumentoDTO request) {
        try {
            // 1. Validar nombre único
            if (request.getId() == null && tipoDocumentoRepository.existsByNombre(request.getNombre())) {
                throw new CreateEntityException("TipoDocumento", 
                        "Ya existe un tipo de documento con el nombre: " + request.getNombre());
            }
            
            // 2, 3, 4 y 5 son configuraciones en los campos del DTO que vienen de quien lo llama
            
            // Si no viene el estado, establecer como ACTIVO por defecto
            if (request.getEstado() == null) {
                request.setEstado(EstadoTiposDocumentoEnum.ACTIVO);
            }
            
            TipoDocumento entity;
            
            // Actualizar o crear
            if (request.getId() != null) {
                entity = tipoDocumentoRepository.findById(request.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Tipo de documento no encontrado con id=" + request.getId()));
                
                // Actualizar campos
                entity.setNombre(request.getNombre());
                entity.setDescripcion(request.getDescripcion());
                entity.setEstado(request.getEstado());
                
                // Registrar auditoría
                registrarAuditoria("tipos_documentos", AccionAuditoriaEnum.UPDATE);
            } else {
                // Crear nuevo
                entity = tipoDocumentoMapper.toModel(request);
                
                // Registrar auditoría
                registrarAuditoria("tipos_documentos", AccionAuditoriaEnum.INSERT);
            }
            
            // Guardar y devolver
            entity = tipoDocumentoRepository.save(entity);
            return tipoDocumentoMapper.toDTO(entity);
            
        } catch (ResourceNotFoundException | CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException("TipoDocumento", 
                    "Error al configurar el tipo de documento: " + e.getMessage());
        }
    }

    public List<TipoDocumentoDTO> obtenerTiposObligatorios() {
        // Obtener todos los tipos activos
        List<TipoDocumento> tiposActivos = tipoDocumentoRepository.findByEstado(EstadoTiposDocumentoEnum.ACTIVO);
        
        // Filtrar los que son obligatorios (descripción contiene "obligatorio")
        return tiposActivos.stream()
                .filter(tipo -> tipo.getDescripcion().toLowerCase().contains("obligatorio"))
                .map(tipoDocumentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TipoDocumentoDTO> obtenerTiposPorEtapa(String etapaProceso) {
        // Obtener todos los tipos activos
        List<TipoDocumento> tiposActivos = tipoDocumentoRepository.findByEstado(EstadoTiposDocumentoEnum.ACTIVO);
        
        // Filtrar por etapa (buscando en la descripción)
        return tiposActivos.stream()
                .filter(tipo -> tipo.getDescripcion().toLowerCase().contains(etapaProceso.toLowerCase()))
                .map(tipoDocumentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TipoDocumentoDTO> obtenerTiposPorCategoria(CategoriaDocumentoEnum categoria) {
        // Obtener todos los tipos activos
        List<TipoDocumento> tiposActivos = tipoDocumentoRepository.findByEstado(EstadoTiposDocumentoEnum.ACTIVO);
        
        // Filtrar por categoría (buscando en la descripción)
        String categoriaStr = categoria.getValor().toLowerCase();
        
        return tiposActivos.stream()
                .filter(tipo -> tipo.getDescripcion().toLowerCase().contains(categoriaStr))
                .map(tipoDocumentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void activarDesactivarTipo(Integer idTipo, boolean activo) {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipo)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Tipo de documento no encontrado con id=" + idTipo));
            
            // Cambiar estado sin eliminar históricos
            tipoDocumento.setEstado(activo ? EstadoTiposDocumentoEnum.ACTIVO : EstadoTiposDocumentoEnum.INACTIVO);
            tipoDocumentoRepository.save(tipoDocumento);
            
            // Registrar auditoría
            registrarAuditoria("tipos_documentos", AccionAuditoriaEnum.UPDATE);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("TipoDocumento", 
                    "Error al cambiar el estado del tipo de documento: " + e.getMessage());
        }
    }

    // === CONTRATOS Y FIRMA ===
    public Map<String, Object> generarContrato(Integer idSolicitud) {
        try {
            // 1. Simular obtención de datos de solicitud aprobada
            // En un sistema real, aquí se consultaría a la solicitud de crédito
            log.info("Generando contrato para solicitud: {}", idSolicitud);
            
            // 2. Generar contrato desde plantilla (simulado)
            String rutaContrato = generarNombreUnico("contrato", idSolicitud) + ".pdf";
            
            // 3. Generar pagarés por cada cuota (simulado)
            String rutaPagare = generarNombreUnico("pagare", idSolicitud) + ".pdf";
            
            // 4. Almacenar en expediente - simulado
            // En un sistema real, aquí se generarían documentos PDF reales
            
            // Registrar en base de datos (simulado)
            DocumentoAdjuntoDTO contratoDTO = new DocumentoAdjuntoDTO();
            contratoDTO.setIdSolicitud(idSolicitud);
            contratoDTO.setIdTipoDocumento(1); // Asumiendo ID 1 para tipo "Contrato"
            contratoDTO.setRutaArchivo(DOCUMENTOS_BASE_PATH + rutaContrato);
            contratoDTO.setFechaCargado(LocalDateTime.now());
            
            // Registrar auditoría
            registrarAuditoria("documentos_adjuntos", AccionAuditoriaEnum.INSERT);
            
            // Crear respuesta
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("idSolicitud", idSolicitud);
            respuesta.put("fechaGeneracion", LocalDateTime.now());
            respuesta.put("documentosGenerados", List.of("Contrato", "Pagaré"));
            respuesta.put("rutaContrato", rutaContrato);
            respuesta.put("rutaPagare", rutaPagare);
            
            return respuesta;
            
        } catch (Exception e) {
            throw new CreateEntityException("Contrato", 
                    "Error al generar el contrato: " + e.getMessage());
        }
    }

    @Transactional
    public void registrarDocumentosFirmados(Integer idSolicitud, List<MultipartFile> documentos) {
        try {
            // 1. Validar plazo máximo siguiente día laborable
            if (!validarPlazoCarga(idSolicitud, LocalDateTime.now())) {
                throw new CreateEntityException("Documento", 
                        "Ha superado el plazo máximo para cargar documentos firmados");
            }
            
            // 2. Cargar documentos firmados
            for (MultipartFile documento : documentos) {
                // Validar formato y guardar
                if (validarFormatoPDF(documento)) {
                    String nombreUnico = generarNombreUnico(documento.getOriginalFilename(), idSolicitud);
                    String rutaArchivo = guardarArchivo(documento, nombreUnico);
                    
                    // Crear registro en BD
                    DocumentoAdjuntoDTO documentoDTO = new DocumentoAdjuntoDTO();
                    documentoDTO.setIdSolicitud(idSolicitud);
                    documentoDTO.setIdTipoDocumento(1); // Aquí debería determinarse el tipo según el archivo
                    documentoDTO.setRutaArchivo(rutaArchivo);
                    documentoDTO.setFechaCargado(LocalDateTime.now());
                    
                    DocumentoAdjunto entity = documentoMapper.toModel(documentoDTO);
                    documentoRepository.save(entity);
                }
            }
            
            // 3. Verificar completitud contractual
            verificarCompletitudDocumental(idSolicitud);
            
            // 4. Notificar para revisión final (simulado)
            log.info("Documentos firmados recibidos para solicitud {}: {}", idSolicitud, documentos.size());
            
            // Registrar auditoría
            registrarAuditoria("documentos_adjuntos", AccionAuditoriaEnum.INSERT);
            
        } catch (IOException e) {
            throw new CreateEntityException("Documento", 
                    "Error al procesar los documentos firmados: " + e.getMessage());
        }
    }

    // === CONSULTAS ===
    public List<DocumentoAdjuntoDTO> listarDocumentosPorSolicitud(Integer idSolicitud) {
        return documentoRepository.findByIdSolicitudOrderByFechaCargadoDesc(idSolicitud)
                .stream()
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DocumentoAdjuntoDTO obtenerDocumento(Integer idDocumento) {
        DocumentoAdjunto documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Documento no encontrado con id=" + idDocumento));
        
        return documentoMapper.toDTO(documento);
    }

    public byte[] descargarArchivo(Integer idDocumento, String usuario) {
        try {
            // 1. Obtener documento
            DocumentoAdjunto documento = documentoRepository.findById(idDocumento)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Documento no encontrado con id=" + idDocumento));
            
            // 1. Validar permisos usuario
            // En un sistema real, aquí se validarían los permisos del usuario
            log.info("Usuario {} descargando documento {}", usuario, idDocumento);
            
            // 2. Desencriptar si es necesario
            String rutaArchivo = documento.getRutaArchivo();
            TipoDocumento tipoDocumento = documento.getTipoDocumento();
            boolean esDocumentoSensible = tipoDocumento != null && 
                    (tipoDocumento.getDescripcion().toLowerCase().contains("buró") ||
                     tipoDocumento.getDescripcion().toLowerCase().contains("confidencial"));
            
            // Cargar archivo
            Path archivoPath = Paths.get(rutaArchivo);
            byte[] contenido = Files.readAllBytes(archivoPath);
            
            // Desencriptar si es necesario (simulado)
            if (esDocumentoSensible) {
                // Simulación de desencriptación
                log.info("Desencriptando documento sensible: {}", idDocumento);
            }
            
            // 3. Registrar acceso para auditoría
            registrarAuditoria("documentos_adjuntos", AccionAuditoriaEnum.SELECT);
            
            return contenido;
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new ResourceNotFoundException("No se pudo leer el archivo del documento: " + e.getMessage());
        }
    }

    // === VALIDACIONES PRIVADAS ===
    private boolean validarFormatoPDF(MultipartFile archivo) {
        String contentType = archivo.getContentType();
        return contentType != null && contentType.equals("application/pdf");
    }

    private boolean validarIntegridadArchivo(MultipartFile archivo) {
        // En un sistema real, aquí se validaría la integridad del archivo
        // Por ejemplo, verificando checksums, ausencia de malware, etc.
        return archivo.getSize() > 0;
    }

    private String generarNombreUnico(String nombreOriginal, Integer idSolicitud) {
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        
        String timestamp = LocalDateTime.now().toString().replace(":", "-").replace(".", "-");
        return idSolicitud + "_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
    }

    private void encriptarDocumentoSensible(String rutaArchivo, Integer idTipoDocumento) {
        // Simulación de encriptación para documentos sensibles
        log.info("Encriptando documento sensible tipo {}: {}", idTipoDocumento, rutaArchivo);
        // En un sistema real, aquí se implementaría la encriptación del archivo
    }

    private boolean validarPlazoCarga(Integer idSolicitud, LocalDateTime fechaCarga) {
        // En un sistema real, aquí se validaría si la fecha de carga está dentro del plazo
        // permitido según el estado de la solicitud
        
        // Simulación: documentos se pueden cargar hasta 3 días después de la fecha actual
        LocalDateTime fechaLimite = LocalDate.now().plusDays(3).atTime(23, 59, 59);
        return fechaCarga.isBefore(fechaLimite);
    }

    private String guardarArchivo(MultipartFile archivo, String nombreArchivo) throws IOException {
        // Crear directorio si no existe
        Path directorioPath = Paths.get(DOCUMENTOS_BASE_PATH);
        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath);
        }
        
        // Guardar archivo
        Path archivoPath = directorioPath.resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), archivoPath);
        
        return archivoPath.toString();
    }

    /**
     * Registra auditoría de una operación
     */
    private void registrarAuditoria(String tabla, AccionAuditoriaEnum accion) {
        AuditoriaDTO audDto = new AuditoriaDTO();
        audDto.setTabla(tabla);
        audDto.setAccion(accion);
        audDto.setFechaHora(LocalDateTime.now());
        auditoriaService.createAuditoria(audDto);
    }

    public List<TipoDocumentoDTO> listarTodosTiposDocumento() {
        return tipoDocumentoRepository.findAll().stream()
                .map(tipoDocumentoMapper::toDTO)
                .toList();
    }

    public List<DocumentoAdjuntoDTO> listarTodosDocumentos() {
        return documentoRepository.findAll().stream()
                .map(documentoMapper::toDTO)
                .toList();
    }
} 