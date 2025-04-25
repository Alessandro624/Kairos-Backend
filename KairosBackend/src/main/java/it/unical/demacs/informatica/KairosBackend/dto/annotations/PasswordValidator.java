package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private int min;
    private int max;
    private boolean requireUpper;
    private boolean requireLower;
    private boolean requireNumber;
    private boolean requireSpecial;
    private String allowedSymbols;

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
            setMessage(context, "Password cannot be null");
            return false;
        }

        if (value.length() < min || value.length() > max) {
            setMessage(context, "Password must be between " + min + " and " + max + " characters");
            return false;
        }

        if (requireUpper && value.chars().noneMatch(Character::isUpperCase)) {
            setMessage(context, "Password must contain at least one uppercase letter");
            return false;
        }

        if (requireLower && value.chars().noneMatch(Character::isLowerCase)) {
            setMessage(context, "Password must contain at least one lowercase letter");
            return false;
        }

        if (requireNumber && value.chars().noneMatch(Character::isDigit)) {
            setMessage(context, "Password must contain at least one digit");
            return false;
        }

        if (requireSpecial && value.chars().noneMatch(c -> allowedSymbols.indexOf(c) >= 0)) {
            setMessage(context, "Password must contain at least one special character (e.g. @#$%^&+=)");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
