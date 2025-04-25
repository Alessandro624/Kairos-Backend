package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "Invalid password";

    int min() default 8;

    int max() default 50;

    boolean requireUpper() default true;

    boolean requireLower() default true;

    boolean requireNumber() default true;

    boolean requireSpecial() default true;

    String allowedSymbols() default "@#$%^&+=";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
