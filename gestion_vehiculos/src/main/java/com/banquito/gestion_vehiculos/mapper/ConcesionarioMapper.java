package com.banquito.gestion_vehiculos.mapper;

import com.banquito.gestion_vehiculos.dto.ConcesionarioDTO;
import com.banquito.gestion_vehiculos.model.Concesionario;
import org.springframework.stereotype.Component;

@Component
public class ConcesionarioMapper {

    public ConcesionarioDTO toDTO(Concesionario model) {
        if (model == null) {
            return null;
        }

        ConcesionarioDTO dto = new ConcesionarioDTO();
        dto.setId(model.getId());
        dto.setRazonSocial(model.getRazonSocial());
        dto.setDireccion(model.getDireccion());
        dto.setTelefono(model.getTelefono());
        dto.setEmailContacto(model.getEmailContacto());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());
        dto.setRuc(model.getRuc());

        return dto;
    }

    public Concesionario toModel(ConcesionarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Concesionario model = new Concesionario();
        model.setId(dto.getId());
        model.setRazonSocial(dto.getRazonSocial());
        model.setDireccion(dto.getDireccion());
        model.setTelefono(dto.getTelefono());
        model.setEmailContacto(dto.getEmailContacto());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());
        model.setRuc(dto.getRuc());

        return model;
    }
}
