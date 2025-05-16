package it.unical.demacs.informatica.KairosBackend.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unical.demacs.informatica.KairosBackend.core.service.CustomOAuth2UserService;
import it.unical.demacs.informatica.KairosBackend.core.service.JwtService;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.dto.auth.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.debug("Handling OAuth2 Authentication Success from URI: {}", request.getRequestURI());

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        Provider provider = Provider.valueOf(registrationId.toUpperCase());
        log.debug("Provider OAuth2 found: {}", provider);

        User user = customOAuth2UserService.getUserByOAuth2User(oAuth2User.getAttributes(), provider);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        log.debug("Generated JWT token for user: {}", userDetails.getUsername());

        AuthResponse authResponse = new AuthResponse(token, refreshToken);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), authResponse);
        log.debug("Authentication Response sent to user: {}", userDetails.getUsername());
    }
}
