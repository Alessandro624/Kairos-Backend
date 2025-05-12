package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Ticket;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findAllByUserParticipant(User user, Pageable pageable);
    Page<Ticket> findAllByEventParticipated(Event event, Pageable pageable);
    Page<Ticket> findAllByIssueDate(LocalDate issueDate, Pageable pageable);

}
