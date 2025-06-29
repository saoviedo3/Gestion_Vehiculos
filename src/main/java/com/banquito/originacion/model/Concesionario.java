package com.banquito.originacion.model;

import com.banquito.originacion.enums.EstadoConcesionarioEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "concesionarios", schema = "originacion")
@Getter
@Setter
public class Concesionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_concesionario", nullable = false)
    private Integer id;

    @Column(name = "razon_social", length = 80, nullable = false)
    private String razonSocial;

    @Column(name = "direccion", length = 120, nullable = false)
    private String direccion;

    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Column(name = "email_contacto", length = 50, nullable = false)
    private String emailContacto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoConcesionarioEnum estado;

    @Version
    private Long version;

    public Concesionario() {
    }

    public Concesionario(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Concesionario that = (Concesionario) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Concesionario{" +
                "id=" + id +
                ", razonSocial='" + razonSocial + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", emailContacto='" + emailContacto + '\'' +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
} 