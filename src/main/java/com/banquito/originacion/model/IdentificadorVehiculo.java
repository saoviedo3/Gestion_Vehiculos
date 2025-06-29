package com.banquito.originacion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "identificadores_vehiculos", schema = "originacion")
@Getter
@Setter
public class IdentificadorVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_identificador_vehiculo", nullable = false)
    private Integer id;

    @Column(name = "vin", length = 17, nullable = false, unique = true)
    private String vin;

    @Column(name = "numero_motor", length = 20, nullable = false, unique = true)
    private String numeroMotor;

    @Column(name = "placa", length = 7, nullable = false, unique = true)
    private String placa;

    @Version
    private Long version;

    public IdentificadorVehiculo() {
    }

    public IdentificadorVehiculo(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IdentificadorVehiculo that = (IdentificadorVehiculo) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "IdentificadorVehiculo{" +
                "id=" + id +
                ", vin='" + vin + '\'' +
                ", numeroMotor='" + numeroMotor + '\'' +
                ", placa='" + placa + '\'' +
                ", version=" + version +
                '}';
    }
} 