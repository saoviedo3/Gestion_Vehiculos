package com.banquito.gestion_vehiculos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CedulaEcuatorianaValidator implements ConstraintValidator<CedulaEcuatoriana, String> {

    @Override
    public boolean isValid(String cedula, ConstraintValidatorContext context) {
        if (cedula == null || cedula.length() != 10) return false;
        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2));
            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
            if (provincia < 1 || provincia > 24 || tercerDigito >= 6) return false;

            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int valor = Integer.parseInt(cedula.substring(i, i + 1));
                if (i % 2 == 0) {
                    valor *= 2;
                    if (valor > 9) valor -= 9;
                }
                suma += valor;
            }
            int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
            int decenaSuperior = ((suma + 9) / 10) * 10;
            int resultado = decenaSuperior - suma;
            if (resultado == 10) resultado = 0;
            return resultado == digitoVerificador;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 