package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EventImageRepository extends JpaRepository<EventImageRepository, UUID> {
    List<EventImage> findByEvent_Id(UUID eventId);
}
