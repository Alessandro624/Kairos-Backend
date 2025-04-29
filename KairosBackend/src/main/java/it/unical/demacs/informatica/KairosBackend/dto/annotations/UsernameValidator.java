package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    private int min;
    private int max;
    private boolean allowOnlySafeCharacters;
    private boolean mustStartWithLetter;
    private String allowedRegex;

    @Override
    public void initialize(Username constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.allowOnlySafeCharacters = constraintAnnotation.allowOnlySafeCharacters();
        this.mustStartWithLetter = constraintAnnotation.mustStartWithLetter();
        this.allowedRegex = constraintAnnotation.allowedRegex();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            setMessage(context, "Username cannot be null");
            return false;
        }

        if (username.length() < min || username.length() > max) {
            setMessage(context, "Username must be between " + min + " and " + max);
            return false;
        }

        if (mustStartWithLetter && !Character.isLetter(username.charAt(0))) {
            setMessage(context, "Username must start with letter");
            return false;
        }

        if (allowOnlySafeCharacters && !username.matches(allowedRegex)) {
            setMessage(context, "Username must contain only safe characters");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
