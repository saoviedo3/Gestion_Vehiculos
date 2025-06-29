package com.banquito.originacion.enums;

public enum EstadoTiposDocumentoEnum {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String valor;

    EstadoTiposDocumentoEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 