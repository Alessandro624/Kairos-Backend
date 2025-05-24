package it.unical.demacs.informatica.KairosBackend.core.service;

import it.unical.demacs.informatica.KairosBackend.config.i18n.MessageReader;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final MessageReader messageReader;

    public UUID getCurrentUserId() {
        log.info("Getting current user id");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            log.warn("Not authenticated");
            throw new IllegalStateException(messageReader.getMessage("auth.not_authenticated"));
        } else {
            UUID id = userRepository.findByUsername(userDetails.getUsername())
                    .map(User::getId)
                    .orElse(null);
            log.info("Found user id: {}", id);
            return id;
        }
    }
}
