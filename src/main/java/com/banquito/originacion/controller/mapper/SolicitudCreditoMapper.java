package com.banquito.originacion.controller.mapper;

import com.banquito.originacion.controller.dto.SolicitudCreditoDTO;
import com.banquito.originacion.model.SolicitudCredito;
import org.springframework.stereotype.Component;

@Component
public class SolicitudCreditoMapper {

    public SolicitudCreditoDTO toDTO(SolicitudCredito model) {
        if (model == null) {
            return null;
        }

        SolicitudCreditoDTO dto = new SolicitudCreditoDTO();
        dto.setId(model.getId());
        dto.setIdClienteProspecto(model.getIdClienteProspecto());
        dto.setIdVehiculo(model.getIdVehiculo());
        dto.setIdVendedor(model.getIdVendedor());
        dto.setNumeroSolicitud(model.getNumeroSolicitud());
        dto.setMontoSolicitado(model.getMontoSolicitado());
        dto.setPlazoMeses(model.getPlazoMeses());
        dto.setFechaSolicitud(model.getFechaSolicitud());
        dto.setEntrada(model.getEntrada());
        dto.setScoreInterno(model.getScoreInterno());
        dto.setScoreExterno(model.getScoreExterno());
        dto.setRelacionCuotaIngreso(model.getRelacionCuotaIngreso());
        dto.setTasaAnual(model.getTasaAnual());
        dto.setCuotaMensual(model.getCuotaMensual());
        dto.setTotalPagar(model.getTotalPagar());
        dto.setEstado(model.getEstado());
        dto.setVersion(model.getVersion());

        return dto;
    }

    public SolicitudCredito toModel(SolicitudCreditoDTO dto) {
        if (dto == null) {
            return null;
        }

        SolicitudCredito model = new SolicitudCredito();
        model.setId(dto.getId());
        model.setIdClienteProspecto(dto.getIdClienteProspecto());
        model.setIdVehiculo(dto.getIdVehiculo());
        model.setIdVendedor(dto.getIdVendedor());
        model.setNumeroSolicitud(dto.getNumeroSolicitud());
        model.setMontoSolicitado(dto.getMontoSolicitado());
        model.setPlazoMeses(dto.getPlazoMeses());
        model.setFechaSolicitud(dto.getFechaSolicitud());
        model.setEntrada(dto.getEntrada());
        model.setScoreInterno(dto.getScoreInterno());
        model.setScoreExterno(dto.getScoreExterno());
        model.setRelacionCuotaIngreso(dto.getRelacionCuotaIngreso());
        model.setTasaAnual(dto.getTasaAnual());
        model.setCuotaMensual(dto.getCuotaMensual());
        model.setTotalPagar(dto.getTotalPagar());
        model.setEstado(dto.getEstado());
        model.setVersion(dto.getVersion());

        return model;
    }
} 