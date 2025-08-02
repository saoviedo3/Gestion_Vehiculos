package com.banquito.gestion_vehiculos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RucEcuatorianoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RucEcuatoriano {
    String message() default "RUC ecuatoriano inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 