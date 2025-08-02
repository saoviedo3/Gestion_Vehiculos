package com.banquito.gestion_vehiculos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RucEcuatorianoValidator implements ConstraintValidator<RucEcuatoriano, String> {
    @Override
    public boolean isValid(String ruc, ConstraintValidatorContext context) {
        if (ruc == null || ruc.length() != 13) return false;
        try {
            // Los primeros 10 dígitos deben ser una cédula válida
            String cedula = ruc.substring(0, 10);
            CedulaEcuatorianaValidator cedulaValidator = new CedulaEcuatorianaValidator();
            if (!cedulaValidator.isValid(cedula, context)) return false;
            // Los últimos 3 dígitos deben ser 001 para personas naturales
            String sufijo = ruc.substring(10);
            return sufijo.equals("001");
        } catch (Exception e) {
            return false;
        }
    }
} 