package it.unical.demacs.informatica.KairosBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.unical.demacs.informatica.KairosBackend.core.service.AuthService;
import it.unical.demacs.informatica.KairosBackend.data.services.UserService;
import it.unical.demacs.informatica.KairosBackend.dto.ServiceError;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserUpdateDTO;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/users", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "Operations related to users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users. Requires ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role",
                            content = @Content(schema = @Schema()))
            }
    )
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @Parameter(description = "Page number (default: 0)") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (default: 10, max: 30)") @RequestParam(value = "size", defaultValue = "10") @Max(30) int size,
            @Parameter(description = "Field to sort by (default: username)") @RequestParam(value = "sortBy", defaultValue = "username") String sortBy,
            @Parameter(description = "Sort direction (default: ASC)") @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction
    ) {
        log.info("Finding all users. Page: {}, Size: {}, SortBy: {}, Direction: {}", page, size, sortBy, direction);
        return ResponseEntity.ok(userService.findAllUsers(PageRequest.of(page, size, direction, sortBy)));
    }

    @Operation(
            summary = "Get all admin users",
            description = "Retrieves a paginated list of all users with the ADMIN role. Requires ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of admin users",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role",
                            content = @Content(schema = @Schema()))
            }
    )
    @GetMapping("/admin")
    public ResponseEntity<Page<UserDTO>> getAllUsersAdmin(
            @Parameter(description = "Page number (default: 0)") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (default: 10, max: 30)") @RequestParam(value = "size", defaultValue = "10") @Max(30) int size,
            @Parameter(description = "Field to sort by (default: username)") @RequestParam(value = "sortBy", defaultValue = "username") String sortBy,
            @Parameter(description = "Sort direction (default: ASC)") @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction
    ) {
        log.info("Finding all users with role ADMIN. Page: {}, Size: {}, SortBy: {}, Direction: {}", page, size, sortBy, direction);
        return ResponseEntity.ok(userService.findAllUsersAdmin(PageRequest.of(page, size, direction, sortBy)));
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a specific user by their ID. Requires ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted the user"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable UUID userId) {
        log.info("Deleting user with id {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update a user",
            description = "Updates a specific user's information. Only the user themselves can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated the user",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ServiceError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Cannot update other users",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @PutMapping("/{userId}")
    @PreAuthorize("#userId == @authService.getCurrentUserId()")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable UUID userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User details to update", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUpdateDTO.class)))
            @Valid @RequestBody UserUpdateDTO userDTO
    ) {
        log.info("Updating user with id {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @Operation(
            summary = "Make a user an admin",
            description = "Elevates a specific user's role to ADMIN. Requires ADMIN role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully made the user an admin",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @PutMapping("/{userId}/make-admin")
    public ResponseEntity<UserDTO> makeUserAdmin(@Parameter(description = "ID of the user to make admin") @PathVariable UUID userId) {
        log.info("Making user with id {} an admin", userId);
        return ResponseEntity.ok(userService.makeUserAdmin(userId));
    }

    @Operation(
            summary = "Get current user's information",
            description = "Retrieves the information of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the current user's information",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ServiceError.class)))
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        log.info("Getting current user");
        return ResponseEntity.ok(userService.findById(authService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }
}
