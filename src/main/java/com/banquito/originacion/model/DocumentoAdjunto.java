package com.banquito.originacion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "documentos_adjuntos", schema = "originacion")
@Getter
@Setter
public class DocumentoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento", nullable = false)
    private Integer id;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "id_tipo_documento", nullable = false)
    private Integer idTipoDocumento;

    @Column(name = "ruta_archivo", length = 150, nullable = false)
    private String rutaArchivo;

    @Column(name = "fecha_cargado", nullable = false)
    private LocalDateTime fechaCargado;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud", insertable = false, updatable = false)
    private SolicitudCredito solicitudCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento", insertable = false, updatable = false)
    private TipoDocumento tipoDocumento;

    public DocumentoAdjunto() {
    }

    public DocumentoAdjunto(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DocumentoAdjunto that = (DocumentoAdjunto) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DocumentoAdjunto{" +
                "id=" + id +
                ", idSolicitud=" + idSolicitud +
                ", idTipoDocumento=" + idTipoDocumento +
                ", rutaArchivo='" + rutaArchivo + '\'' +
                ", fechaCargado=" + fechaCargado +
                ", version=" + version +
                '}';
    }
} 