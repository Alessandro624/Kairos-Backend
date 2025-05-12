package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.ticket.TicketDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {

    Optional<TicketDTO> findById(UUID id);
    Page<TicketDTO> findAllByUserParticipant(UserDTO user, Pageable pageable);
    Page<TicketDTO> findAllByIssueDate(LocalDate issueDate, Pageable pageable);
    Page<TicketDTO> findAllByEventParticipated(EventDTO event, Pageable pageable);

    TicketDTO createTicket(TicketDTO ticketDTO);

    void deleteTicket(UUID id); // only for admin & organizer (??)
}
