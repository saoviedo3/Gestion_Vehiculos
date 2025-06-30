package com.banquito.gestion_vehiculos.mapper;

import org.springframework.stereotype.Component;

import com.banquito.gestion_vehiculos.dto.IdentificadorVehiculoDTO;
import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;

@Component
public class IdentificadorVehiculoMapper {

    public IdentificadorVehiculoDTO toDTO(IdentificadorVehiculo model) {
        if (model == null) {
            return null;
        }

        IdentificadorVehiculoDTO dto = new IdentificadorVehiculoDTO();
        dto.setId(model.getId());
        dto.setVin(model.getVin());
        dto.setNumeroMotor(model.getNumeroMotor());
        dto.setPlaca(model.getPlaca());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public IdentificadorVehiculo toModel(IdentificadorVehiculoDTO dto) {
        if (dto == null) {
            return null;
        }

        IdentificadorVehiculo model = new IdentificadorVehiculo();
        model.setId(dto.getId());
        model.setVin(dto.getVin());
        model.setNumeroMotor(dto.getNumeroMotor());
        model.setPlaca(dto.getPlaca());
        model.setVersion(dto.getVersion());

        return model;
    }
}
