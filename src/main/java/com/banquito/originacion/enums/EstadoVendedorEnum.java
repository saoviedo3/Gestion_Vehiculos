package com.banquito.originacion.enums;

public enum EstadoVendedorEnum {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String valor;

    EstadoVendedorEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 