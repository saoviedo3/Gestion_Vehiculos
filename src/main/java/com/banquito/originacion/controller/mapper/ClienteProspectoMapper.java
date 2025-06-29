package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.ClienteProspectoDTO;
import com.banquito.originacion.model.ClienteProspecto;
import org.springframework.stereotype.Component;

@Component
public class ClienteProspectoMapper {

    public ClienteProspectoDTO toDTO(ClienteProspecto model) {
        if (model == null) {
            return null;
        }

        ClienteProspectoDTO dto = new ClienteProspectoDTO();
        dto.setId(model.getId());
        dto.setCedula(model.getCedula());
        dto.setNombre(model.getNombre());
        dto.setApellido(model.getApellido());
        dto.setTelefono(model.getTelefono());
        dto.setEmail(model.getEmail());
        dto.setDireccion(model.getDireccion());
        dto.setIngresos(model.getIngresos());
        dto.setEgresos(model.getEgresos());
        dto.setActividadEconomica(model.getActividadEconomica());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public ClienteProspecto toModel(ClienteProspectoDTO dto) {
        if (dto == null) {
            return null;
        }

        ClienteProspecto model = new ClienteProspecto();
        model.setId(dto.getId());
        model.setCedula(dto.getCedula());
        model.setNombre(dto.getNombre());
        model.setApellido(dto.getApellido());
        model.setTelefono(dto.getTelefono());
        model.setEmail(dto.getEmail());
        model.setDireccion(dto.getDireccion());
        model.setIngresos(dto.getIngresos());
        model.setEgresos(dto.getEgresos());
        model.setActividadEconomica(dto.getActividadEconomica());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        return model;
    }
} 