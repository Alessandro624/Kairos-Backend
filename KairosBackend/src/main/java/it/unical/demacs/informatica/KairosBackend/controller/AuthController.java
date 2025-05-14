package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.core.service.JwtService;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.data.services.UserService;
import it.unical.demacs.informatica.KairosBackend.dto.auth.AuthRequest;
import it.unical.demacs.informatica.KairosBackend.dto.auth.AuthResponse;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/auth", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        log.debug("Processing login request for user: {}", authRequest.getUsernameOrEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.getPassword()));
        log.debug("Authentication successful for user: {}", authRequest.getUsernameOrEmail());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.debug("Generating JWT tokens for user: {}", userDetails.getUsername());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        log.info("Login successful for user: {}", userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        log.debug("Processing token refresh request");
        if (!jwtService.isTokenValid(refreshToken)) {
            log.warn("Invalid refresh token provided");
            return ResponseEntity.badRequest().build();
        }
        String username = jwtService.extractUsername(refreshToken);
        log.debug("Refresh token valid for user: {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newToken = jwtService.generateToken(userDetails);
        log.info("Token refresh successful for user: {}", username);
        return ResponseEntity.ok(new AuthResponse(newToken, refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        log.debug("Processing registration request for username: {}", userCreateDTO.getUsername());
        userCreateDTO.setProvider(Provider.LOCAL);
        userCreateDTO.setRole(UserRole.USER);
        userService.createUser(userCreateDTO);
        log.info("User registration successful for username: {}", userCreateDTO.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout/success")
    public ResponseEntity<String> logoutSuccess() {
        log.info("User logged out successfully");
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/oauth2/login/failure")
    public ResponseEntity<String> oAuth2LoginFailure() {
        log.warn("OAuth2 login attempt failed");
        return ResponseEntity.badRequest().body("Login with OAuth2 failed");
    }
}
