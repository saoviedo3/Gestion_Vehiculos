package com.banquito.originacion.model;

import com.banquito.originacion.enums.EstadoClientesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "clientes_prospectos", schema = "originacion")
@Getter
@Setter
public class ClienteProspecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente_prospecto", nullable = false)
    private Integer id;

    @Column(name = "cedula", length = 10, nullable = false, unique = true)
    private String cedula;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Column(name = "direccion", length = 120, nullable = false)
    private String direccion;

    @Column(name = "ingresos", precision = 12, scale = 2, nullable = false)
    private BigDecimal ingresos;

    @Column(name = "egresos", precision = 12, scale = 2, nullable = false)
    private BigDecimal egresos;

    @Column(name = "actividad_economica", length = 120, nullable = false)
    private String actividadEconomica;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoClientesEnum estado;

    @Version
    private Long version;

    public ClienteProspecto() {
    }

    public ClienteProspecto(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClienteProspecto that = (ClienteProspecto) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ClienteProspecto{" +
                "id=" + id +
                ", cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ingresos=" + ingresos +
                ", egresos=" + egresos +
                ", actividadEconomica='" + actividadEconomica + '\'' +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
} 