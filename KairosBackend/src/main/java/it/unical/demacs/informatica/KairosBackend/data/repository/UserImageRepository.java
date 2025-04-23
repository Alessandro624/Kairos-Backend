package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserImageRepository extends JpaRepository<UserImage, UUID> {
    Optional<UserImage> findByUserId(UUID userId);
}
