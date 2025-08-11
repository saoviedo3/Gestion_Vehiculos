package com.banquito.gestion_vehiculos.mapper;

import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;
import com.banquito.gestion_vehiculos.dto.IdentificadorVehiculoDTO;
import org.springframework.stereotype.Component;

@Component
public class IdentificadorVehiculoMapper {
    public IdentificadorVehiculoDTO toDTO(IdentificadorVehiculo model) {
        if (model == null) return null;
        IdentificadorVehiculoDTO dto = new IdentificadorVehiculoDTO();
        dto.setPlaca(model.getPlaca());
        dto.setChasis(model.getChasis());
        dto.setMotor(model.getMotor());
        dto.setId(model.getId());
        return dto;
    }

    public IdentificadorVehiculo toModel(IdentificadorVehiculoDTO dto) {
        if (dto == null) return null;
        IdentificadorVehiculo model = new IdentificadorVehiculo();
        model.setPlaca(dto.getPlaca());
        model.setChasis(dto.getChasis());
        model.setMotor(dto.getMotor());
        model.setId(dto.getId());
        return model;
    }
} 