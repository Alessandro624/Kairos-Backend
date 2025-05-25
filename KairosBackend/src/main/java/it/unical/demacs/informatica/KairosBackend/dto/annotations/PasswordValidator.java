package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import it.unical.demacs.informatica.KairosBackend.config.i18n.MessageReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<Password, String> {
    private int min;
    private int max;
    private boolean requireUpper;
    private boolean requireLower;
    private boolean requireNumber;
    private boolean requireSpecial;
    private String allowedSymbols;

    private final MessageReader messageReader;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.requireUpper = constraintAnnotation.requireUpper();
        this.requireLower = constraintAnnotation.requireLower();
        this.requireNumber = constraintAnnotation.requireNumber();
        this.requireSpecial = constraintAnnotation.requireSpecial();
        this.allowedSymbols = constraintAnnotation.allowedSymbols();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            setMessage(context, "validation.password.null");
            return false;
        }

        if (value.length() < min || value.length() > max) {
            setMessage(context, "validation.password.length", "min", String.valueOf(min), "max", String.valueOf(max));
            return false;
        }

        if (requireUpper && value.chars().noneMatch(Character::isUpperCase)) {
            setMessage(context, "validation.password.upper");
            return false;
        }

        if (requireLower && value.chars().noneMatch(Character::isLowerCase)) {
            setMessage(context, "validation.password.lower");
            return false;
        }

        if (requireNumber && value.chars().noneMatch(Character::isDigit)) {
            setMessage(context, "validation.password.number");
            return false;
        }

        if (requireSpecial && value.chars().noneMatch(c -> allowedSymbols.indexOf(c) >= 0)) {
            setMessage(context, "validation.password.special", "allowedSymbols", allowedSymbols);
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String messageKey) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageReader.getMessage(messageKey))
                .addConstraintViolation();
    }

    private void setMessage(ConstraintValidatorContext context, String messageKey, String... args) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageReader.getMessage(messageKey, (Object) args))
                .addConstraintViolation();
    }
}
