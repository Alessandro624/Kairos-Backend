package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserDTO> findById(UUID id);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    UserDTO updateUser(UUID userId, UserUpdateDTO userDTO);

    UserDTO createUser(UserCreateDTO userDTO);

    // TODO maybe using pageable
    List<User> findAllUsersAdmin();

    // TODO maybe using pageable
    List<UserDTO> findAllUsers();

    // TODO only if needed
    void deleteUser(UUID userId);
}
