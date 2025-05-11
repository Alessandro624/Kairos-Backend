package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import it.unical.demacs.informatica.KairosBackend.data.repository.UserRepository;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserUpdateDTO;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceAlreadyExistsException;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsernameOrEmail(String usernameOrEmail) {
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional
    public UserDTO updateUser(UUID userId, UserUpdateDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserCreateDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ResourceAlreadyExistsException("Username " + userDTO.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Email " + userDTO.getEmail() + " already exists");
        }
        User newUser = modelMapper.map(userDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // TODO check provider if local then this should be false
        newUser.setEmailVerified(true);
        User savedUser = userRepository.save(newUser);
        // TODO send verification email or handle it with another service
        // TODO handle other object creations
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsersAdmin(Pageable pageable) {
        Page<User> users = userRepository.findAllByRole(UserRole.ADMIN, pageable);
        return users.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        // TODO clean other things (profile image, wishlist, ..)
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
