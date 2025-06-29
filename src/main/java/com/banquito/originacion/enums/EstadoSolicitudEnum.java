package com.banquito.originacion.enums;

public enum EstadoSolicitudEnum {
    BORRADOR("Borrador"),
    EN_REVISION("EnRevision"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada"),
    CANCELADA("Cancelada"),
    INSTRUMENTADA("Instrumentada");

    private final String valor;

    EstadoSolicitudEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
} 