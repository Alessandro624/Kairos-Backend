package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // TODO maybe adding a findByUsernameOrEmail

    // TODO it can be useful for a cleaning scheduler
    Page<User> findByEmailVerifiedFalse(Pageable pageable);

    // TODO find all by role ?
    // Page<User> findByRole(UserRole provider, Pageable pageable);
}
