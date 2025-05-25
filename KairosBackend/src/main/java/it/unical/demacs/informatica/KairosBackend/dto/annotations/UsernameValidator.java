package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import it.unical.demacs.informatica.KairosBackend.config.i18n.MessageReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<Username, String> {
    private int min;
    private int max;
    private boolean allowOnlySafeCharacters;
    private boolean mustStartWithLetter;
    private String allowedRegex;

    private final MessageReader messageReader;

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
            setMessage(context, "validation.username.null");
            return false;
        }

        if (username.length() < min || username.length() > max) {
            setMessage(context, "validation.username.length", "min", String.valueOf(min), "max", String.valueOf(max));
            return false;
        }

        if (mustStartWithLetter && (username.isEmpty() || !Character.isLetter(username.charAt(0)))) {
            setMessage(context, "validation.username.muststartwithletter");
            return false;
        }

        if (allowOnlySafeCharacters && !username.matches(allowedRegex)) {
            setMessage(context, "validation.username.safecharacters");
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
