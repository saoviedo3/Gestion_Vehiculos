package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.model.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaDTO toDTO(Auditoria model) {
        if (model == null) {
            return null;
        }

        AuditoriaDTO dto = new AuditoriaDTO();
        dto.setId(model.getId());
        dto.setTabla(model.getTabla());
        dto.setAccion(model.getAccion());
        dto.setFechaHora(model.getFechaHora());

        return dto;
    }

    public Auditoria toModel(AuditoriaDTO dto) {
        if (dto == null) {
            return null;
        }

        Auditoria model = new Auditoria();
        model.setId(dto.getId());
        model.setTabla(dto.getTabla());
        model.setAccion(dto.getAccion());
        model.setFechaHora(dto.getFechaHora());

        return model;
    }
} 