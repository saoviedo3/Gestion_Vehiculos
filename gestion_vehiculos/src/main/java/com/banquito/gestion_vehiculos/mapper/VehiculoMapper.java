package com.banquito.gestion_vehiculos.mapper;

import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;
import com.banquito.gestion_vehiculos.model.Vehiculo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class VehiculoMapper {

    public VehiculoDTO toDTO(Vehiculo model) {
        if (model == null) return null;
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(model.getId());
        dto.setMarca(model.getMarca());
        dto.setModelo(model.getModelo());
        dto.setAnio(model.getAnio());
        dto.setValor(model.getValor());
        dto.setColor(model.getColor());
        dto.setExtras(model.getExtras());
        dto.setEstado(model.getEstado());
        dto.setTipo(model.getTipo());
        dto.setCombustible(model.getCombustible());
        dto.setVersion(model.getVersion());
        if (model.getIdentificadorVehiculo() != null)
            dto.setIdentificadorVehiculo(new IdentificadorVehiculoMapper().toDTO(model.getIdentificadorVehiculo()));
        return dto;
    }

    public Vehiculo toModel(VehiculoDTO dto) {
        if (dto == null) return null;
        Vehiculo model = new Vehiculo();
        model.setId(dto.getId());
        model.setMarca(dto.getMarca());
        model.setModelo(dto.getModelo());
        model.setAnio(dto.getAnio());
        model.setValor(dto.getValor());
        model.setColor(dto.getColor());
        model.setExtras(dto.getExtras());
        model.setEstado(dto.getEstado());
        model.setTipo(dto.getTipo());
        model.setCombustible(dto.getCombustible());
        model.setVersion(dto.getVersion());
        if (dto.getIdentificadorVehiculo() != null)
            model.setIdentificadorVehiculo(new IdentificadorVehiculoMapper().toModel(dto.getIdentificadorVehiculo()));
        return model;
    }

    public List<VehiculoDTO> toDTOList(List<Vehiculo> entityList) {
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void updateEntity(Vehiculo entity, VehiculoDTO dto, IdentificadorVehiculo identificador) {
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setAnio(dto.getAnio());
        entity.setValor(dto.getValor());
        entity.setColor(dto.getColor());
        entity.setExtras(dto.getExtras());
        entity.setEstado(dto.getEstado());
        entity.setTipo(dto.getTipo());
        entity.setCombustible(dto.getCombustible());
        entity.setVersion(dto.getVersion());
        if (identificador != null) entity.setIdentificadorVehiculo(identificador);
    }
}
