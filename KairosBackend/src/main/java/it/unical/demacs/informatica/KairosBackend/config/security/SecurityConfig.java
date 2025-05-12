package it.unical.demacs.informatica.KairosBackend.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unical.demacs.informatica.KairosBackend.config.filter.JwtAuthFilter;
import it.unical.demacs.informatica.KairosBackend.dto.ServiceError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private final CorsConfigurationSource corsConfigurationSource;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationHandler oAuth2AuthenticationHandler;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CORS
                .cors(c -> c.configurationSource(corsConfigurationSource))
                // CSFR DISABLE FOR STATELESS API
                .csrf(AbstractHttpConfigurer::disable)
                // STATELESS
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // EXCEPTION HANDLER
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setHeader("WWW-Authenticate", "Basic realm=\"Access to /login authentication endpoint\"");
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            ServiceError error = new ServiceError();
                            error.setTimestamp(Date.from(Instant.now()));
                            error.setMessage(authException.getMessage());
                            error.setUrl(request.getRequestURI());
                            ObjectMapper mapper = new ObjectMapper();
                            response.getWriter().write(mapper.writeValueAsString(error));
                        }))
                // AUTHORIZATION TODO add other endpoints
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login", "/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/auth/oauth2/**").permitAll()
                        .requestMatchers("/swagger.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                // JWT FILTER BEFORE LOGIN
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // OAUTH2 LOGIN HANDLING
                .oauth2Login(o -> o
                        .authorizationEndpoint(a -> a.baseUri("/v1/auth/oauth2/authorize"))
                        .redirectionEndpoint(r -> r.baseUri("/v1/auth/oauth2/callback/*"))
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationHandler)
                        .failureUrl("/v1/auth/oauth2/login/failure")
                )
                // OAUTH2 RESOURCE SERVER FOR KEYCLOAK
                .oauth2ResourceServer(oauth2 -> oauth2
                        // EXCLUDING JWT DECODING BY ISSUER
                        .bearerTokenResolver(request -> {
                            String token = request.getHeader("Authorization");
                            if (token == null) {
                                return null;
                            }
                            try {
                                String issuer = extractIssuer(token);
                                if (!issuer.equals(issuerUri)) {
                                    return null;
                                }
                            } catch (Exception ignoredException) {
                                return null;
                            }
                            return token.startsWith("Bearer ") ? token.substring(7) : null;
                        })
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                // LOGOUT HANDLING
                .logout(l -> l
                        .logoutUrl("/v1/auth/logout")
                        .logoutSuccessUrl("/v1/auth/logout/success")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    private String extractIssuer(String token) {
        try {
            String[] chunks = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(payload);
            return json.get("iss").asText();
        } catch (Exception e) {
            throw new JwtException("Unable to extract issuer from token", e);
        }
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
