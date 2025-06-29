package com.banquito.originacion.enums;

public enum EstadoConcesionarioEnum {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String valor;

    EstadoConcesionarioEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 