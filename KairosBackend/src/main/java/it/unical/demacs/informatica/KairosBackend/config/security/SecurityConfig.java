package it.unical.demacs.informatica.KairosBackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unical.demacs.informatica.KairosBackend.config.filter.JwtAuthFilter;
import it.unical.demacs.informatica.KairosBackend.config.filter.KeycloakJwtFilter;
import it.unical.demacs.informatica.KairosBackend.config.handler.OAuth2AuthenticationHandler;
import it.unical.demacs.informatica.KairosBackend.core.service.CustomOAuth2UserService;
import it.unical.demacs.informatica.KairosBackend.dto.ServiceError;
import it.unical.demacs.informatica.KairosBackend.utility.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.Instant;
import java.util.Date;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private final KeycloakJwtFilter keycloakJwtFilter;

    private final CorsConfigurationSource corsConfigurationSource;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationHandler oAuth2AuthenticationHandler;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");

        if (!securityProperties.isEnabled()) {
            log.warn("Security is disabled: testing purpose only");
            return http
                    .cors(c -> c.configurationSource(corsConfigurationSource))
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                    .build();
        }

        return http
                // CORS
                .cors(c -> {
                    log.debug("Configuring CORS with custom configuration source");
                    c.configurationSource(corsConfigurationSource);
                })
                // CSFR DISABLE FOR STATELESS API
                .csrf(csrf -> {
                    log.debug("Disabling CSRF protection for stateless API");
                    csrf.disable();
                })
                // STATELESS
                .sessionManagement(s -> {
                    log.debug("Setting session management policy to STATELESS");
                    s.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })// EXCEPTION HANDLER
                .exceptionHandling(e -> {
                    log.debug("Configuring custom authentication entry point");
                    e.authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication failure: {} for request URI: {}", authException.getMessage(), request.getRequestURI());
                        response.setHeader("WWW-Authenticate", "Basic realm=\"Access to /login authentication endpoint\"");
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        ServiceError error = new ServiceError();
                        error.setTimestamp(Date.from(Instant.now()));
                        error.setMessage(authException.getMessage());
                        error.setUrl(request.getRequestURI());
                        ObjectMapper mapper = new ObjectMapper();
                        response.getWriter().write(mapper.writeValueAsString(error));
                    });
                })
                // AUTHORIZATION
                .authorizeHttpRequests(a -> {
                    log.debug("Configuring HTTP authorization rules");
                    for (String path : securityProperties.getPublicEndpoints()) {
                        a.requestMatchers(path).permitAll();
                        log.debug("Permitting public access to {}", path);
                    }
                    securityProperties.getProtectedRoutes().forEach((path, roles) -> {
                        String[] roleArray = roles.split(",\\s*");
                        if (path.contains("/POST")) {
                            String actualPath = path.replace("/POST", "");
                            a.requestMatchers(HttpMethod.POST, actualPath).hasAnyAuthority(roleArray);
                            log.debug("Protecting POST {} with roles {}", actualPath, roles);
                        } else if (path.contains("/PUT")) {
                            String actualPath = path.replace("/PUT", "");
                            a.requestMatchers(HttpMethod.PUT, actualPath).hasAnyAuthority(roleArray);
                            log.debug("Protecting PUT {} with roles {}", actualPath, roles);
                        } else if (path.contains("/DELETE")) {
                            String actualPath = path.replace("/DELETE", "");
                            a.requestMatchers(HttpMethod.DELETE, actualPath).hasAnyAuthority(roleArray);
                            log.debug("Protecting DELETE {} with roles {}", actualPath, roles);
                        } else {
                            a.requestMatchers(path).hasAnyAuthority(roleArray);
                            log.debug("Protecting {} with roles {}", path, roles);
                        }
                    });
                    a.anyRequest().authenticated();
                    log.debug("Requiring authentication for all other requests");
                })
                // JWT FILTER BEFORE LOGIN
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // OAUTH2 LOGIN HANDLING
                .oauth2Login(o -> {
                    log.debug("Configuring OAuth2 login handling");
                    o.authorizationEndpoint(a -> a.baseUri("/v1/auth/oauth2/authorize"));
                    o.redirectionEndpoint(r -> r.baseUri("/v1/auth/oauth2/callback/*"));
                    o.userInfoEndpoint(u -> u.userService(customOAuth2UserService));
                    o.successHandler(oAuth2AuthenticationHandler);
                    o.failureUrl("/v1/auth/oauth2/login/failure");
                    log.debug("OAuth2 login configured with custom user service and authentication handler");
                })
                // OAUTH2 RESOURCE SERVER FOR KEYCLOAK
                .oauth2ResourceServer(oauth2 -> {
                    log.debug("Configuring OAuth2 resource server for Keycloak");
                    // EXCLUDING JWT DECODING BY ISSUER
                    oauth2.bearerTokenResolver(request -> {
                        String token = request.getHeader("Authorization");
                        if (token != null && SecurityUtils.isKeycloakToken(token, issuerUri)) {
                            log.debug("Found Keycloak token, bypassing standard JWT decoding");
                            return token.startsWith("Bearer ") ? token.substring(7) : null;
                        }
                        log.debug("No token or non-Keycloak token found in request");
                        return null;
                    });
                    oauth2.jwt(jwt -> {
                        log.debug("Configuring JWT authentication converter");
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                    });
                })
                .addFilterAfter(keycloakJwtFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        log.info("Creating JWT decoder from issuer URI: {}", issuerUri);
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        log.debug("Creating JWT authentication converter");
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        log.info("Creating authentication manager");
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        log.debug("Creating BCrypt password encoder");
        return new BCryptPasswordEncoder();
    }
}
