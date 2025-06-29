package com.banquito.originacion.enums;

public enum CategoriaDocumentoEnum {
    PERSONAL("Documentos Personales"),
    VEHICULO("Documentos del Vehículo"),
    CONTRACTUAL("Documentos Contractuales");

    private final String valor;

    CategoriaDocumentoEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 