package it.unical.demacs.informatica.KairosBackend.config.auditor;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserAuditorAware implements AuditorAware<String> {
    // TODO check if this is the best way to do it or if we have to store the id in the security context instead of the email/username

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            return Optional.empty();

        return Optional.of(authentication.getName());
    }
}
