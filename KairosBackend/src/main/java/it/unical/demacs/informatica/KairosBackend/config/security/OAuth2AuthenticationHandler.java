package it.unical.demacs.informatica.KairosBackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.dto.auth.AuthResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        User user = customOAuth2UserService.getUserByOAuth2User(oAuth2User, provider);

        if (user.getProvider() != provider) {
            throw new OAuth2AuthenticationException("User with email " + user.getEmail() + " already exists");
        }

        OAuth2User updatedOAuth2User = customOAuth2UserService.getOAuth2User(oAuth2User, user);

        OAuth2AuthenticationToken newToken = new OAuth2AuthenticationToken(
                updatedOAuth2User,
                updatedOAuth2User.getAuthorities(),
                registrationId);

        SecurityContextHolder.getContext().setAuthentication(newToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        AuthResponse authResponse = new AuthResponse(token, refreshToken);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(authResponse));

        // super.onAuthenticationSuccess(request, response, newToken);
    }
}
