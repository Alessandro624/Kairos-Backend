package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import it.unical.demacs.informatica.KairosBackend.config.i18n.MessageReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private int min;
    private int max;
    private boolean mustStartWithPlus;
    private String regex;

    private final MessageReader messageReader;

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
            setMessage(context, "validation.phonenumber.length", "min", String.valueOf(min), "max", String.valueOf(max));
            return false;
        }

        if (mustStartWithPlus && !phone.startsWith("+")) {
            setMessage(context, "validation.phonenumber.muststartwithplus");
            return false;
        }

        if (regex != null && !phone.matches(regex)) {
            setMessage(context, "validation.phonenumber.invalidcharacters");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String messageKey) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageReader.getMessage(messageKey))
                .addConstraintViolation();
    }

    @SuppressWarnings("SameParameterValue")
    private void setMessage(ConstraintValidatorContext context, String messageKey, String... args) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageReader.getMessage(messageKey, (Object) args))
                .addConstraintViolation();
    }
}
