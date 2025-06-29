package com.banquito.originacion.enums;

public enum EstadoClientesEnum {
    ACTIVO("Activo"),
    NUEVO("Nuevo"),
    ACTIVO_CREDITOS_VENCIDOS("ActivoCreditosVencidos"),
    PROSPECTO("Prospecto");

    private final String valor;

    EstadoClientesEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 