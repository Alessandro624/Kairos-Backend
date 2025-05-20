package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.config.CacheConfig;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${kairos.cleanup.email-verification.delay:900000}")
    private long userVerificationEmailExpiration;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(UUID id) {
        log.info("Finding user with id {}", id);
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsername(String username) {
        log.info("Finding user with username {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        log.info("Finding user with email {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsernameOrEmail(String usernameOrEmail) {
        log.info("Find user with username or email: {}", usernameOrEmail);
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        return user.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional
    public UserDTO updateUser(UUID userId, UserUpdateDTO userDTO) {
        log.info("Updating user with id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        User savedUser = userRepository.save(user);
        log.info("Updated user with id {}", userId);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void updateUserPassword(UUID userId, String oldPassword, String newPassword) {
        log.info("Updating password for user with id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (user.getProvider() != Provider.LOCAL) {
            throw new IllegalArgumentException("Only local users can update password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Updated password for user with id {}", userId);
    }

    @Override
    public UserDTO makeUserAdmin(UUID userId) {
        log.info("Making user with id {} an admin", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        user.setRole(UserRole.ADMIN);
        User savedUser = userRepository.save(user);
        log.info("Made user with id {} an admin", userId);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserCreateDTO userDTO) {
        log.info("Creating user {}", userDTO);
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ResourceAlreadyExistsException("Username " + userDTO.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Email " + userDTO.getEmail() + " already exists");
        }
        User newUser = modelMapper.map(userDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setEmailVerified(true);
        User savedUser = userRepository.save(newUser);
        log.info("Created user {}", savedUser);
        // TODO send verification email or handle it with another service
        // TODO handle other object creations
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_FOR_USER, key = "#pageable")
    public Page<UserDTO> findAllUsersAdmin(Pageable pageable) {
        log.info("Finding all users with role ADMIN");
        Page<User> users = userRepository.findAllByRole(UserRole.ADMIN, pageable);
        return users.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_FOR_USER, key = "#pageable")
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        log.info("Finding all users");
        Page<User> users = userRepository.findAll(pageable);
        return users.map(u -> modelMapper.map(u, UserDTO.class));
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheConfig.CACHE_FOR_USER, allEntries = true)
    public void deleteUser(UUID userId) {
        log.info("Deleting user with id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        // TODO clean other things (profile image, wishlist, ..)
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUsername(String username) {
        log.info("Checking if username {} exists", username);
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsEmail(String email) {
        log.info("Checking if email {} exists", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${kairos.cleanup.email-verification.delay}", initialDelayString = "${kairos.cleanup.email-verification.initial-delay}")
    public void cleanUpUnverifiedUsers() {
        log.info("Starting cleanup of unverified user accounts");

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(userVerificationEmailExpiration);
        List<User> unverifiedUsers = userRepository.findAllByEmailVerifiedFalseAndCreationDateBefore(cutoffTime);

        if (!unverifiedUsers.isEmpty()) {
            log.info("Found {} unverified user accounts to delete", unverifiedUsers.size());
            userRepository.deleteAll(unverifiedUsers);
            log.info("Successfully deleted {} unverified user accounts", unverifiedUsers.size());
        } else {
            log.info("No unverified user accounts to delete");
        }
    }
}
