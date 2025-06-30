package com.banquito.gestion_vehiculos.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.TipoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CombustibleVehiculoEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Document(collection = "vehiculos")
public class Vehiculo {

    private String id;
    private String marca;
    private String modelo;
    private double cilindraje;
    private String anio;
    private BigDecimal valor;
    private String color;
    private String extras;
    private EstadoVehiculoEnum estado;
    private TipoVehiculoEnum tipo;
    private CombustibleVehiculoEnum combustible;
    private Identificador identificadorVehiculo;
    private Long version;

    public static class Identificador {
        private String placa;
        private String chasis;
        private String motor;
        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }
        public String getChasis() { return chasis; }
        public void setChasis(String chasis) { this.chasis = chasis; }
        public String getMotor() { return motor; }
        public void setMotor(String motor) { this.motor = motor; }
    }

    public Identificador getIdentificadorVehiculo() { return identificadorVehiculo; }
    public void setIdentificadorVehiculo(Identificador identificadorVehiculo) { this.identificadorVehiculo = identificadorVehiculo; }

}
