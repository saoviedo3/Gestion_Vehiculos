package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.VehiculoDTO;
import com.banquito.originacion.model.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class VehiculoMapper {

    public VehiculoDTO toDTO(Vehiculo model) {
        if (model == null) {
            return null;
        }

        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(model.getId());
        dto.setIdConcesionario(model.getIdConcesionario());
        dto.setIdIdentificadorVehiculo(model.getIdIdentificadorVehiculo());
        dto.setMarca(model.getMarca());
        dto.setModelo(model.getModelo());
        dto.setAnio(model.getAnio());
        dto.setValor(model.getValor());
        dto.setColor(model.getColor());
        dto.setExtras(model.getExtras());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public Vehiculo toModel(VehiculoDTO dto) {
        if (dto == null) {
            return null;
        }

        Vehiculo model = new Vehiculo();
        model.setId(dto.getId());
        model.setIdConcesionario(dto.getIdConcesionario());
        model.setIdIdentificadorVehiculo(dto.getIdIdentificadorVehiculo());
        model.setMarca(dto.getMarca());
        model.setModelo(dto.getModelo());
        model.setAnio(dto.getAnio());
        model.setValor(dto.getValor());
        model.setColor(dto.getColor());
        model.setExtras(dto.getExtras());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        return model;
    }
} 