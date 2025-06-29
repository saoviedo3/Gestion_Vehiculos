package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.DocumentoAdjuntoDTO;
import com.banquito.originacion.model.DocumentoAdjunto;
import org.springframework.stereotype.Component;

@Component
public class DocumentoAdjuntoMapper {

    public DocumentoAdjuntoDTO toDTO(DocumentoAdjunto model) {
        if (model == null) {
            return null;
        }

        DocumentoAdjuntoDTO dto = new DocumentoAdjuntoDTO();
        dto.setId(model.getId());
        dto.setIdSolicitud(model.getIdSolicitud());
        dto.setIdTipoDocumento(model.getIdTipoDocumento());
        dto.setRutaArchivo(model.getRutaArchivo());
        dto.setFechaCargado(model.getFechaCargado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public DocumentoAdjunto toModel(DocumentoAdjuntoDTO dto) {
        if (dto == null) {
            return null;
        }

        DocumentoAdjunto model = new DocumentoAdjunto();
        model.setId(dto.getId());
        model.setIdSolicitud(dto.getIdSolicitud());
        model.setIdTipoDocumento(dto.getIdTipoDocumento());
        model.setRutaArchivo(dto.getRutaArchivo());
        model.setFechaCargado(dto.getFechaCargado());
        model.setVersion(dto.getVersion());

        return model;
    }
} 