package it.unical.demacs.informatica.KairosBackend.config.handler;

import it.unical.demacs.informatica.KairosBackend.core.service.CustomOAuth2UserService;
import it.unical.demacs.informatica.KairosBackend.core.service.JwtService;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${app.oauth2.authorizedRedirectUris}")
    private String authorizedRedirectUris;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.debug("Handling OAuth2 Authentication Success from URI: {}", request.getRequestURI());

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String requestedRedirectUri = (String) request.getSession().getAttribute("redirect_uri_before_oauth");
        if (requestedRedirectUri == null) {
            // if not in session try URL
            requestedRedirectUri = request.getParameter("redirect_uri");
        }

        if (requestedRedirectUri == null || requestedRedirectUri.isEmpty()) {
            log.error("Redirect URI non found");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Redirect URI is mandatory");
            return;
        }

        List<String> authorizedUris = Arrays.asList(authorizedRedirectUris.split(","));
        if (!authorizedUris.contains(requestedRedirectUri)) {
            log.error("Redirect URI not authorized: {}", requestedRedirectUri);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Redirect URI not authorized");
            return;
        }

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        Provider provider = Provider.valueOf(registrationId.toUpperCase());
        log.debug("Provider OAuth2 found: {}", provider);

        User user = customOAuth2UserService.getUserByOAuth2User(oAuth2User.getAttributes(), provider);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        log.debug("Generated JWT token for user: {}", userDetails.getUsername());

        String targetUrl = UriComponentsBuilder.fromUriString(requestedRedirectUri)
                .queryParam("token", token)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        log.debug("Redirecting to client with URL: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
