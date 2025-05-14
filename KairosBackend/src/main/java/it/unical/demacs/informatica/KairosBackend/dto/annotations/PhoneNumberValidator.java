package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private int min;
    private int max;
    private boolean mustStartWithPlus;
    private String regex;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.mustStartWithPlus = constraintAnnotation.mustStartWithPlus();
        this.regex = constraintAnnotation.regex();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return true;
        }

        if (phone.length() < min || phone.length() > max) {
            setMessage(context, "Phone number must be between " + min + " and " + max + " characters");
            return false;
        }

        if (mustStartWithPlus && !phone.startsWith("+")) {
            setMessage(context, "Phone number must start with '+'");
            return false;
        }

        if (!phone.matches(regex)) {
            setMessage(context, "Phone number contains invalid characters");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
