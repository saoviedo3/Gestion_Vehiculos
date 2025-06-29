package com.banquito.originacion.enums;

public enum AccionAuditoriaEnum {
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    SELECT("SELECT");

    private final String valor;

    AccionAuditoriaEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 