package it.unical.demacs.informatica.KairosBackend.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface EventImageRepository extends JpaRepository<EventImageRepository, UUID> {
    Optional<EventImageRepository> findByEventId(UUID eventId);
}
