package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.TipoDocumentoDTO;
import com.banquito.originacion.model.TipoDocumento;
import org.springframework.stereotype.Component;

@Component
public class TipoDocumentoMapper {

    public TipoDocumentoDTO toDTO(TipoDocumento model) {
        if (model == null) {
            return null;
        }

        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setDescripcion(model.getDescripcion());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public TipoDocumento toModel(TipoDocumentoDTO dto) {
        if (dto == null) {
            return null;
        }

        TipoDocumento model = new TipoDocumento();
        model.setId(dto.getId());
        model.setNombre(dto.getNombre());
        model.setDescripcion(dto.getDescripcion());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        return model;
    }
} 