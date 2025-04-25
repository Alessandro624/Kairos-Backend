package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
    String message() default "Invalid username";

    int min() default 4;

    int max() default 30;

    String allowedRegex() default "^[a-zA-Z0-9._-]+$";

    boolean allowOnlySafeCharacters() default true;

    boolean mustStartWithLetter() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
