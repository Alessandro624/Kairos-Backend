package it.unical.demacs.informatica.KairosBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.unical.demacs.informatica.KairosBackend.core.service.EmailService;
import it.unical.demacs.informatica.KairosBackend.core.service.JwtService;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.data.services.UserService;
import it.unical.demacs.informatica.KairosBackend.dto.ServiceError;
import it.unical.demacs.informatica.KairosBackend.dto.auth.AuthRequest;
import it.unical.demacs.informatica.KairosBackend.dto.auth.AuthResponse;
import it.unical.demacs.informatica.KairosBackend.dto.auth.PasswordResetConfirmation;
import it.unical.demacs.informatica.KairosBackend.dto.auth.PasswordResetRequest;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/v1/auth", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Operations related to authentication")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final EmailService emailService;

    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns JWT and refresh tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials",
                            content = @Content(schema = @Schema(implementation = ServiceError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema()))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Authentication request", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthRequest.class)))
            @Valid @RequestBody AuthRequest authRequest
    ) {
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

    @Operation(
            summary = "Refresh JWT token",
            description = "Refreshes the JWT token using a valid refresh token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully refreshed token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid refresh token",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token", required = true,
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            @RequestBody String refreshToken
    ) {
        log.debug("Processing token refresh request");
        if (!jwtService.isTokenValid(refreshToken, "refresh")) {
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

    @Operation(
            summary = "Register new user",
            description = "Registers a new user account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully registered"),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration details", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCreateDTO.class)))
            @Valid @RequestBody UserCreateDTO userCreateDTO
    ) {
        log.debug("Processing registration request for username: {}", userCreateDTO.getUsername());
        userCreateDTO.setProvider(Provider.LOCAL);
        userCreateDTO.setRole(UserRole.USER);
        userService.createUser(userCreateDTO);
        log.info("User registration successful for username: {}", userCreateDTO.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Request password reset",
            description = "Initiates the password reset process by sending a reset link to the user's email.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's username or email to send the reset link to", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasswordResetRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset link sent successfully (or if user not found, to prevent enumeration)"),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequest request) {
        log.info("Password reset request for: {}", request.getUsernameOrEmail());

        userService.findByUsernameOrEmail(request.getUsernameOrEmail())
                .ifPresent(userDTO -> {
                    if (userDTO.getProvider() == Provider.LOCAL) {
                        String resetToken = jwtService.generatePasswordResetToken(userDTO.getUsername());
                        String resetLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/v1/auth/reset-password")
                                .queryParam("token", resetToken)
                                .toUriString();

                        log.info("Sending password reset email to '{}'", userDTO.getEmail());
                        emailService.sendPasswordResetEmail(userDTO.getEmail(), userDTO.getUsername(), resetLink);
                    }
                });

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Confirm password reset",
            description = "Resets the user's password using a valid reset token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New password and the reset token", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasswordResetConfirmation.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password successfully reset"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired token, or invalid input",
                            content = @Content(schema = @Schema(implementation = ServiceError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetConfirmation request) {
        log.debug("Processing password reset confirmation");

        if (!jwtService.isTokenValid(request.getToken(), "reset-password")) {
            log.warn("Invalid or expired password reset token: {}", request.getToken());
            return ResponseEntity.badRequest().build();
        }

        String username = jwtService.extractUsername(request.getToken());

        userService.resetUserPassword(username, request.getNewPassword());
        log.info("Password successfully reset for user: {}", username);
        return ResponseEntity.ok().build();

    }

    @Operation(
            summary = "Confirm user email",
            description = "Confirms a user's email address using a verification token sent to their email.",
            parameters = {
                    @Parameter(name = "token", description = "The email verification token", required = true,
                            schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email successfully verified"),
                    @ApiResponse(responseCode = "400", description = "Invalid verification link",
                            content = @Content(schema = @Schema(implementation = ServiceError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@Parameter(hidden = true) @RequestParam String token) {
        log.debug("Processing email confirmation with token: {}", token);

        if (!jwtService.isTokenValid(token, "email-verification")) {
            log.warn("Email verification token is invalid or expired: {}", token);
            return ResponseEntity.badRequest().body("Invalid verification link.");
        }

        String username = jwtService.extractUsername(token);
        userService.activateUser(username);
        log.info("Email verification successful for user: {}", username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Logout success message",
            description = "Endpoint to indicate successful logout.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            }
    )
    @GetMapping("/logout/success")
    public ResponseEntity<String> logoutSuccess() {
        log.info("User logged out successfully");
        return ResponseEntity.ok("Logout successful");
    }

    @Operation(
            summary = "OAuth2 login failure message",
            description = "Endpoint to indicate a failed OAuth2 login attempt.",
            responses = {
                    @ApiResponse(responseCode = "400", description = "OAuth2 login failed",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            }
    )
    @GetMapping("/oauth2/login/failure")
    public ResponseEntity<String> oAuth2LoginFailure() {
        log.warn("OAuth2 login attempt failed");
        return ResponseEntity.badRequest().body("Login with OAuth2 failed");
    }
}
