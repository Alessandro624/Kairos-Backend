package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";

    int min() default 10;

    int max() default 15;

    boolean mustStartWithPlus() default false;

    String regex() default "^\\+?[0-9]+$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
