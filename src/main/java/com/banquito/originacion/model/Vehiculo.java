package com.banquito.originacion.model;

import com.banquito.originacion.enums.EstadoVehiculoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vehiculos", schema = "originacion")
@Getter
@Setter
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo", nullable = false)
    private Integer id;

    @Column(name = "id_concesionario", nullable = false)
    private Integer idConcesionario;

    @Column(name = "id_identificador_vehiculo", nullable = false)
    private Integer idIdentificadorVehiculo;

    @Column(name = "marca", length = 40, nullable = false)
    private String marca;

    @Column(name = "modelo", length = 40, nullable = false)
    private String modelo;

    @Column(name = "anio", precision = 4, nullable = false)
    private Integer anio;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "color", length = 30, nullable = false)
    private String color;

    @Column(name = "extras", length = 150)
    private String extras;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoVehiculoEnum estado;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concesionario", referencedColumnName = "id_concesionario", insertable = false, updatable = false)
    private Concesionario concesionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_identificador_vehiculo", referencedColumnName = "id_identificador_vehiculo", insertable = false, updatable = false)
    private IdentificadorVehiculo identificadorVehiculo;

    public Vehiculo() {
    }

    public Vehiculo(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) obj;
        return Objects.equals(id, vehiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", idConcesionario=" + idConcesionario +
                ", idIdentificadorVehiculo=" + idIdentificadorVehiculo +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio=" + anio +
                ", valor=" + valor +
                ", color='" + color + '\'' +
                ", extras='" + extras + '\'' +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
} 