package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoSwearingValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSwearing {
    String message() default "This field doesn't allow swear words";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
