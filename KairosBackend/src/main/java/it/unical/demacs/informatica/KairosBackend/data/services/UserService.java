package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserDTO> findById(UUID id);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    UserDTO updateUser(UUID userId, UserDTO userDTO);

    // TODO maybe using another DTO for registration
    UserDTO createUser(UserDTO userDTO);

    // TODO maybe using pageable
    List<User> findAllUsersAdmin();

    // TODO maybe using pageable
    List<UserDTO> findAllUsers();

    // TODO only if needed
    void deleteUser(UUID userId);
}
