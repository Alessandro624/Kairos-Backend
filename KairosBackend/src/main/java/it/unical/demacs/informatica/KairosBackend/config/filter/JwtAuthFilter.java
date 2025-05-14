package it.unical.demacs.informatica.KairosBackend.config.filter;

import it.unical.demacs.informatica.KairosBackend.core.service.JwtService;
import it.unical.demacs.informatica.KairosBackend.utility.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthFilter starting for request to: {}", request.getRequestURI());

        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.debug("Found Bearer authorization header");

            final String token = authorizationHeader.substring(7);
            if (!SecurityUtils.isKeycloakToken(token, issuerUri)) {
                log.debug("Processing non-Keycloak JWT token");

                final String username = jwtService.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.debug("Extracted username from token: {}", username);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtService.isTokenValid(token, userDetails)) {
                        Authentication authentication = SecurityUtils.createAuthentication(userDetails, request);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Authentication stored in SecurityContext for user: {}", username);
                    } else {
                        log.warn("Invalid JWT token for user: {}", username);
                    }
                }
            } else {
                log.debug("Skipping Keycloak token - will be handled by KeycloakJwtFilter");
            }
        }
        filterChain.doFilter(request, response);
    }
}
