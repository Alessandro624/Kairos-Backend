package it.unical.demacs.informatica.KairosBackend.core.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpiration;

    @Value("${kairos.cleanup.email-verification.delay}")
    private long emailVerificationExpiration;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), "auth", jwtExpiration, userDetails.getAuthorities());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), "refresh", jwtRefreshExpiration, userDetails.getAuthorities());
    }

    public String generateEmailVerificationToken(String username) {
        return buildToken(username, "email-verification", emailVerificationExpiration, null);
    }

    private String buildToken(String subject, String type, long expiration, Object claims) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .claim("type", type)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey());

        if (claims != null) {
            builder.claim("roles", claims);
        }

        return builder.compact();
    }

    public String extractTokenType(String token) {
        try {
            return extractClaim(token, claims -> claims.get("type", String.class));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isAuthTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        String tokenType = extractTokenType(token);
        return "auth".equals(tokenType) && (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenValid(String token, String type) {
        try {
            extractAllClaims(token);
            return type.equals(extractTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private java.util.Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateCapabilityToken(String resource, String issuer, String id, String... permissions) {
        // TODO how to generate a capability token?
        return null;
    }

    public String getResourceIdFromCapabilityToken(String resource, String issuer, String token, String... permissions) {
        return null;
    }
}
