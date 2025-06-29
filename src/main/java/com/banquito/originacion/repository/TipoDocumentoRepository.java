package com.banquito.originacion.repository;

import com.banquito.originacion.enums.EstadoTiposDocumentoEnum;
import com.banquito.originacion.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {

    /**
     * Busca tipos de documentos por nombre exacto
     */
    Optional<TipoDocumento> findByNombre(String nombre);

    /**
     * Busca tipos de documentos por nombre parcial (case-insensitive)
     */
    List<TipoDocumento> findByNombreContainingIgnoreCase(String nombreParcial);

    /**
     * Busca tipos de documentos por estado
     */
    List<TipoDocumento> findByEstado(EstadoTiposDocumentoEnum estado);

    /**
     * Busca tipos de documentos por descripción parcial (case-insensitive)
     */
    List<TipoDocumento> findByDescripcionContainingIgnoreCase(String descripcionParcial);

    /**
     * Verifica si existe un tipo de documento con el nombre exacto
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca tipos de documentos por nombre parcial y estado
     */
    List<TipoDocumento> findByNombreContainingIgnoreCaseAndEstado(
            String nombreParcial, EstadoTiposDocumentoEnum estado);

    /**
     * Busca tipos de documentos ordenados por nombre
     */
    List<TipoDocumento> findByEstadoOrderByNombreAsc(EstadoTiposDocumentoEnum estado);

    /**
     * Busca tipos de documentos que no contengan cierta palabra en su descripción
     */
    List<TipoDocumento> findByDescripcionNotContaining(String palabra);

    /**
     * Busca tipos de documentos por descripción que empieza con cierto texto (útil para categorías)
     */
    List<TipoDocumento> findByDescripcionStartingWith(String prefijo);

    /**
     * Cuenta el número de tipos de documentos por estado
     */
    long countByEstado(EstadoTiposDocumentoEnum estado);
} 