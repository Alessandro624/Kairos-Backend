package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserDTO> findById(UUID id);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    Optional<UserDTO> findByUsernameOrEmail(String usernameOrEmail);

    UserDTO updateUser(UUID userId, UserUpdateDTO userDTO);

    void updateUserPassword(UUID userId, String oldPassword, String newPassword);

    void resetUserPassword(String username, String newPassword);

    void activateUser(String username);

    UserDTO updateUserRole(UUID userId, UserRole role);

    UserDTO createUser(UserCreateDTO userDTO);

    Page<UserDTO> findAllUsersAdmin(Pageable pageable);

    Page<UserDTO> findAllUsers(Pageable pageable);

    void deleteUser(UUID userId);

    boolean existsUsername(String username);

    boolean existsEmail(String email);

    @SuppressWarnings("unused")
    void cleanUpUnverifiedUsers();
}
