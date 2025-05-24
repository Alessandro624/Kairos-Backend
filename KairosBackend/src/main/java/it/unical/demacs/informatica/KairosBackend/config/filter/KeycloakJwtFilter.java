package it.unical.demacs.informatica.KairosBackend.config.filter;

import it.unical.demacs.informatica.KairosBackend.core.service.CustomOAuth2UserService;
import it.unical.demacs.informatica.KairosBackend.utility.SecurityUtils;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakJwtFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("KeycloakJwtFilter starting for request to: {}", request.getRequestURI());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtToken) {
            log.debug("JWT authentication token found");

            Jwt jwt = jwtToken.getToken();
            Map<String, Object> claims = jwt.getClaims();

            log.debug("Processing JWT claims for subject: {}", jwt.getSubject());
            User user = customOAuth2UserService.getUserByOAuth2User(claims, Provider.KEYCLOAK);

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            Authentication authentication = SecurityUtils.createAuthentication(userDetails, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication updated in SecurityContext for user: {}", user.getUsername());
        } else {
            log.trace("No JWT authentication token found in request");
        }
        filterChain.doFilter(request, response);
    }
}
