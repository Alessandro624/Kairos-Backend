package it.unical.demacs.informatica.KairosBackend.dto.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class NoSwearingValidator implements ConstraintValidator<NoSwearing, String> {
    private final Set<String> blacklist = new HashSet<>();

    @Override
    public void initialize(NoSwearing constraintAnnotation) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("blacklist.txt")) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    reader.lines()
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .filter(line -> !line.isEmpty() && !line.startsWith("#")) // skip empty or comment lines
                            .forEach(blacklist::add);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("blacklist.txt file not found in resources.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load blacklist.txt", e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        String lowerValue = value.toLowerCase();
        return blacklist.stream().noneMatch(lowerValue::contains);
    }
}
