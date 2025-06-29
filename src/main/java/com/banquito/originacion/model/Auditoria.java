package com.banquito.originacion.model;

import com.banquito.originacion.enums.AccionAuditoriaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "auditorias", schema = "originacion")
@Getter
@Setter
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria", nullable = false)
    private Integer id;

    @Column(name = "tabla", length = 40, nullable = false)
    private String tabla;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", length = 6, nullable = false)
    private AccionAuditoriaEnum accion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    public Auditoria() {
    }

    public Auditoria(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Auditoria auditoria = (Auditoria) obj;
        return Objects.equals(id, auditoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Auditoria{" +
                "id=" + id +
                ", tabla='" + tabla + '\'' +
                ", accion=" + accion +
                ", fechaHora=" + fechaHora +
                '}';
    }
} 