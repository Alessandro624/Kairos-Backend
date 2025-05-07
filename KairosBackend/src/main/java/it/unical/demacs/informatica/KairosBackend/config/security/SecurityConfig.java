package it.unical.demacs.informatica.KairosBackend.config.security;

import it.unical.demacs.informatica.KairosBackend.config.filter.JwtAuthFilter;
import it.unical.demacs.informatica.KairosBackend.dto.ServiceError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.Instant;
import java.util.Date;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private final CorsConfigurationSource corsConfigurationSource;

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
                            response.getWriter().write(error.toString());
                        }))
                // AUTHORIZATION TODO add other endpoints
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login", "/v1/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger.html", "/swagger-ui/**", "/api-docs.html", "/actuator").permitAll()
                )
                // JWT FILTER BEFORE LOGIN TODO create a custom filter to allow multiple login providers
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
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
