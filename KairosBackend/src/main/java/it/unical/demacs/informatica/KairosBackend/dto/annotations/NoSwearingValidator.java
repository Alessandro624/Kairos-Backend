package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class NoSwearingValidator implements ConstraintValidator<NoSwearing, String> {
    private static final Set<String> DEFAULT_BLACKLIST = Set.of(
            "cazzo", "cagata"
    ); // it shouldn't be hardcoded... but it's funny

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        String lowerValue = value.toLowerCase();
        return DEFAULT_BLACKLIST.stream().noneMatch(lowerValue::contains);
    }
}
