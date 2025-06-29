package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.VendedorDTO;
import com.banquito.originacion.model.Vendedor;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    public VendedorDTO toDTO(Vendedor model) {
        if (model == null) {
            return null;
        }

        VendedorDTO dto = new VendedorDTO();
        dto.setId(model.getId());
        dto.setIdConcesionario(model.getIdConcesionario());
        dto.setNombre(model.getNombre());
        dto.setTelefono(model.getTelefono());
        dto.setEmail(model.getEmail());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public Vendedor toModel(VendedorDTO dto) {
        if (dto == null) {
            return null;
        }

        Vendedor model = new Vendedor();
        model.setId(dto.getId());
        model.setIdConcesionario(dto.getIdConcesionario());
        model.setNombre(dto.getNombre());
        model.setTelefono(dto.getTelefono());
        model.setEmail(dto.getEmail());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        return model;
    }
} 