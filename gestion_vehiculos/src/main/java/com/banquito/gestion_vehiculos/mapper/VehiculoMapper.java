package com.banquito.gestion_vehiculos.mapper;

import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class VehiculoMapper {

    public VehiculoDTO toDTO(Vehiculo model) {
        if (model == null) {
            return null;
        }

        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(model.getId());
        dto.setMarca(model.getMarca());
        dto.setModelo(model.getModelo());
        dto.setAnio(model.getAnio());
        dto.setValor(model.getValor());
        dto.setColor(model.getColor());
        dto.setExtras(model.getExtras());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        if (model.getIdentificadorVehiculo() != null) {
            dto.setIdentificadorVehiculo(new IdentificadorVehiculoMapper().toDTO(model.getIdentificadorVehiculo()));
        }

        return dto;
    }

    public Vehiculo toModel(VehiculoDTO dto) {
        if (dto == null) {
            return null;
        }

        Vehiculo model = new Vehiculo();
        model.setId(dto.getId());
        model.setMarca(dto.getMarca());
        model.setModelo(dto.getModelo());
        model.setAnio(dto.getAnio());
        model.setValor(dto.getValor());
        model.setColor(dto.getColor());
        model.setExtras(dto.getExtras());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        if (dto.getIdentificadorVehiculo() != null) {
            model.setIdentificadorVehiculo(new IdentificadorVehiculoMapper().toModel(dto.getIdentificadorVehiculo()));
        }

        return model;
    }
}