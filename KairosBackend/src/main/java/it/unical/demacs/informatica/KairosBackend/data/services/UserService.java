package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserDTO> findById(UUID id);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    // TODO maybe a findByUsernameOrEmail for login ?

    // TODO throws UserNotFoundException
    UserDTO updateUser(UUID userId, UserUpdateDTO userDTO);

    // TODO update password maybe with another DTO (oldPass, newPass, confPass), using a token based mechanism?

    // TODO token based mechanism for email verification

    // TODO authentication using a UserAuthDTO with usernameOrEmail and a password field ?

    // TODO throws UserAlreadyExistsException
    UserDTO createUser(UserCreateDTO userDTO);

    Page<UserDTO> findAllUsersAdmin(Pageable pageable);

    Page<UserDTO> findAllUsers(Pageable pageable);

    // TODO only if needed, throws UserNotFoundException
    void deleteUser(UUID userId);

    boolean existsUsername(String username);

    boolean existsEmail(String email);
}
