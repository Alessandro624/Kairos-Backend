package it.unical.demacs.informatica.KairosBackend.config.auditor;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

public class UserAuditorAware implements AuditorAware<UUID> {

    private static final UUID AUTH_CODE = UUID.randomUUID();
    @Override
    public Optional<UUID> getCurrentAuditor() {
        //TODO get logged user (Spring security)
        return Optional.of(AUTH_CODE);
    }
}
