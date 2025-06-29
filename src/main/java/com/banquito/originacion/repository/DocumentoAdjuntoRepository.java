package com.banquito.originacion.repository;

import com.banquito.originacion.model.DocumentoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Integer> {

    /**
     * Busca documentos por id de solicitud
     */
    List<DocumentoAdjunto> findByIdSolicitud(Integer idSolicitud);

    /**
     * Busca documentos por tipo de documento
     */
    List<DocumentoAdjunto> findByIdTipoDocumento(Integer idTipoDocumento);

    /**
     * Busca documentos por tipo de documento y solicitud
     */
    List<DocumentoAdjunto> findByIdSolicitudAndIdTipoDocumento(Integer idSolicitud, Integer idTipoDocumento);

    /**
     * Busca documentos cargados en un rango de fechas
     */
    List<DocumentoAdjunto> findByFechaCargadoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Verifica si existe un documento con una ruta específica
     */
    boolean existsByRutaArchivo(String rutaArchivo);

    /**
     * Busca documentos por solicitud ordenados por fecha de carga descente (más recientes primero)
     */
    List<DocumentoAdjunto> findByIdSolicitudOrderByFechaCargadoDesc(Integer idSolicitud);

    /**
     * Cuenta el número de documentos por solicitud
     */
    long countByIdSolicitud(Integer idSolicitud);

    /**
     * Cuenta el número de documentos por tipo
     */
    long countByIdTipoDocumento(Integer idTipoDocumento);

    /**
     * Busca documentos por solicitud y con ruta que contenga el texto (búsqueda parcial)
     */
    List<DocumentoAdjunto> findByIdSolicitudAndRutaArchivoContaining(Integer idSolicitud, String rutaParcial);

    /**
     * Busca documento específico por solicitud y tipo (debería ser único o el más reciente)
     */
    Optional<DocumentoAdjunto> findFirstByIdSolicitudAndIdTipoDocumentoOrderByFechaCargadoDesc(
            Integer idSolicitud, Integer idTipoDocumento);
} 