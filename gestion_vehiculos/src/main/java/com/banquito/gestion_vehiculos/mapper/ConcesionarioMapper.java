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
        dto.setRazonSocial(model.getRazon_social());
        dto.setDireccion(model.getDireccion());
        dto.setTelefono(model.getTelefono());
        dto.setEmailContacto(model.getEmail_contacto());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public Concesionario toModel(ConcesionarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Concesionario model = new Concesionario();
        model.setId(dto.getId());
        model.setRazon_social(dto.getRazonSocial());
        model.setDireccion(dto.getDireccion());
        model.setTelefono(dto.getTelefono());
        model.setEmail_contacto(dto.getEmailContacto());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        return model;
    }
}
