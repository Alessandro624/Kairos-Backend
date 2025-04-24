package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, PagingAndSortingRepository<Event, UUID> {
    Page<Event> findAllByOrganizer(User user, Pageable pageable);
    Page<Event> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
