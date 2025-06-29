package com.banquito.originacion.model;

import com.banquito.originacion.enums.EstadoSolicitudEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "solicitudes_creditos", schema = "originacion")
@Getter
@Setter
public class SolicitudCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud", nullable = false)
    private Integer id;

    @Column(name = "id_cliente_prospecto", nullable = false)
    private Integer idClienteProspecto;

    @Column(name = "id_vehiculo", nullable = false)
    private Integer idVehiculo;

    @Column(name = "numero_solicitud", length = 50, nullable = false, unique = true)
    private String numeroSolicitud;

    @Column(name = "monto_solicitado", precision = 12, scale = 2, nullable = false)
    private BigDecimal montoSolicitado;

    @Column(name = "plazo_meses", precision = 3, nullable = false)
    private Integer plazoMeses;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "entrada", precision = 12, scale = 2, nullable = false)
    private BigDecimal entrada;

    @Column(name = "score_interno", precision = 6, scale = 2, nullable = false)
    private BigDecimal scoreInterno;

    @Column(name = "score_externo", precision = 6, scale = 2, nullable = false)
    private BigDecimal scoreExterno;

    @Column(name = "relacion_cuota_ingreso", precision = 5, scale = 2, nullable = false)
    private BigDecimal relacionCuotaIngreso;

    @Column(name = "tasa_anual", precision = 5, scale = 2, nullable = false)
    private BigDecimal tasaAnual;

    @Column(name = "cuota_mensual", precision = 8, scale = 2, nullable = false)
    private BigDecimal cuotaMensual;

    @Column(name = "total_pagar", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalPagar;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSolicitudEnum estado;

    @Version
    private Long version;

    @Column(name = "id_vendedor", nullable = false)
    private Integer idVendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_prospecto", referencedColumnName = "id_cliente_prospecto", insertable = false, updatable = false)
    private ClienteProspecto clienteProspecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id_vehiculo", insertable = false, updatable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor", referencedColumnName = "id_vendedor", insertable = false, updatable = false)
    private Vendedor vendedor;

    public SolicitudCredito() {
    }

    public SolicitudCredito(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SolicitudCredito that = (SolicitudCredito) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SolicitudCredito{" +
                "id=" + id +
                ", idClienteProspecto=" + idClienteProspecto +
                ", idVehiculo=" + idVehiculo +
                ", numeroSolicitud='" + numeroSolicitud + '\'' +
                ", montoSolicitado=" + montoSolicitado +
                ", plazoMeses=" + plazoMeses +
                ", fechaSolicitud=" + fechaSolicitud +
                ", entrada=" + entrada +
                ", scoreInterno=" + scoreInterno +
                ", scoreExterno=" + scoreExterno +
                ", relacionCuotaIngreso=" + relacionCuotaIngreso +
                ", tasaAnual=" + tasaAnual +
                ", cuotaMensual=" + cuotaMensual +
                ", totalPagar=" + totalPagar +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
} 