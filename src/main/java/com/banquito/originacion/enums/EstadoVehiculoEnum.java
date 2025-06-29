package com.banquito.originacion.enums;

public enum EstadoVehiculoEnum {
    NUEVO("Nuevo"),
    USADO("Usado");

    private final String valor;

    EstadoVehiculoEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 