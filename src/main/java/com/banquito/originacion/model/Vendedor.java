package com.banquito.originacion.model;

import com.banquito.originacion.enums.EstadoVendedorEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "vendedores", schema = "originacion")
@Getter
@Setter
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vendedor", nullable = false)
    private Integer id;

    @Column(name = "id_concesionario", nullable = false)
    private Integer idConcesionario;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoVendedorEnum estado;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concesionario", referencedColumnName = "id_concesionario", insertable = false, updatable = false)
    private Concesionario concesionario;

    public Vendedor() {
    }

    public Vendedor(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vendedor vendedor = (Vendedor) obj;
        return Objects.equals(id, vendedor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "id=" + id +
                ", idConcesionario=" + idConcesionario +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
} 