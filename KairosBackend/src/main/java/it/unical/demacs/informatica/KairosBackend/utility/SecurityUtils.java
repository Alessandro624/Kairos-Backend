package it.unical.demacs.informatica.KairosBackend.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecurityUtils {
    public static UsernamePasswordAuthenticationToken createAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    public static String extractIssuer(String token) {
        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                throw new JwtException("Invalid JWT token format");
            }

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(payload);

            if (!json.has("iss")) {
                throw new JwtException("Missing issuer claim in JWT");
            }

            return json.get("iss").asText();
        } catch (Exception e) {
            throw new JwtException("Unable to extract issuer from token");
        }
    }

    public static boolean isKeycloakToken(String token, String issuerUri) {
        try {
            String issuer = extractIssuer(token);
            // Check if the issuer is from Keycloak
            return issuer != null && issuer.trim().equals(issuerUri);
        } catch (Exception e) {
            return false;
        }
    }
}
