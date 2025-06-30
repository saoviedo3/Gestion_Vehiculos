package com.banquito.gestion_vehiculos.mapper;

import com.banquito.gestion_vehiculos.dto.VendedorDTO;
import com.banquito.gestion_vehiculos.model.Vendedor;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    public VendedorDTO toDTO(Vendedor model) {
        if (model == null) {
            return null;
        }

        VendedorDTO dto = new VendedorDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setTelefono(model.getTelefono());
        dto.setEmail(model.getEmail());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());
        dto.setCedula(model.getCedula());

        return dto;
    }

    public Vendedor toModel(VendedorDTO dto) {
        if (dto == null) {
            return null;
        }

        Vendedor model = new Vendedor();
        model.setId(dto.getId());
        model.setNombre(dto.getNombre());
        model.setTelefono(dto.getTelefono());
        model.setEmail(dto.getEmail());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());
        model.setCedula(dto.getCedula());

        return model;
    }
}
