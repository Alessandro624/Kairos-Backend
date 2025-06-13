package it.unical.demacs.informatica.KairosBackend.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.io.Serializable;
import java.util.Base64;
import java.util.Optional;

@Component
@Slf4j
public class CustomOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.debug("loadAuthorizationRequest called for URI: {}", request.getRequestURI());

        return Optional.ofNullable(WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME))
                .map(this::deserialize)
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug("saveAuthorizationRequest called. Saving AuthorizationRequest to cookie.");

        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        String serializedAuthorizationRequest = serialize(authorizationRequest);
        addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serializedAuthorizationRequest, COOKIE_EXPIRE_SECONDS);
        log.debug("OAuth2AuthorizationRequest saved in cookie: {}", OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

        if (StringUtils.hasText(redirectUriAfterLogin)) {
            addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
            log.debug("Redirect URI saved in cookie: {}", REDIRECT_URI_PARAM_COOKIE_NAME);
        } else {
            deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.debug("removeAuthorizationRequest called. Removing AuthorizationRequest cookie.");

        OAuth2AuthorizationRequest originalRequest = loadAuthorizationRequest(request);

        deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

        return originalRequest;
    }

    public static void removeAuthorizationRequestCookies(HttpServletRequest request,
                                                         HttpServletResponse response) {
        deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Optional.ofNullable(WebUtils.getCookie(request, name)).ifPresent(cookie -> {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        return Optional.ofNullable(WebUtils.getCookie(request, name));
    }

    private String serialize(OAuth2AuthorizationRequest authorizationRequest) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(authorizationRequest));
    }

    private OAuth2AuthorizationRequest deserialize(Cookie cookie) {
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
            Serializable deserializedObject = (Serializable) SerializationUtils.deserialize(decodedBytes);
            return (OAuth2AuthorizationRequest) deserializedObject;
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode Base64 URL string from cookie: {}. Error: {}", cookie.getValue(), e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Failed to deserialize AuthorizationRequest from cookie: {}. Error: {}", cookie.getValue(), e.getMessage());
            return null;
        }
    }
}
