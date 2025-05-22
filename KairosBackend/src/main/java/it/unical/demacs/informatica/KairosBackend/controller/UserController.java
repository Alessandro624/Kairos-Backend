package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.core.service.AuthService;
import it.unical.demacs.informatica.KairosBackend.data.services.UserService;
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
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") @Max(30) int size,
            @RequestParam(value = "sortBy", defaultValue = "username") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction
    ) {
        log.info("Finding all users. Page: {}, Size: {}, SortBy: {}, Direction: {}", page, size, sortBy, direction);
        return ResponseEntity.ok(userService.findAllUsers(PageRequest.of(page, size, direction, sortBy)));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<UserDTO>> getAllUsersAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") @Max(30) int size,
            @RequestParam(value = "sortBy", defaultValue = "username") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction direction
    ) {
        log.info("Finding all users with role ADMIN. Page: {}, Size: {}, SortBy: {}, Direction: {}", page, size, sortBy, direction);
        return ResponseEntity.ok(userService.findAllUsersAdmin(PageRequest.of(page, size, direction, sortBy)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("Deleting user with id {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#userId == @authService.getCurrentUserId()")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody @Valid UserUpdateDTO userDTO) {
        log.info("Updating user with id {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @PutMapping("/{userId}/make-admin")
    public ResponseEntity<UserDTO> makeUserAdmin(@PathVariable UUID userId) {
        log.info("Making user with id {} an admin", userId);
        return ResponseEntity.ok(userService.makeUserAdmin(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        log.info("Getting current user");
        return ResponseEntity.ok(userService.findById(authService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }
}
