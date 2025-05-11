package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Ticket;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.repository.TicketRepository;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.ticket.TicketDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketDTO> findById(UUID id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.map(t -> modelMapper.map(t, TicketDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDTO> findAllByUserParticipant(UserDTO user, Pageable pageable) {
        User userParticipant = modelMapper.map(user, User.class);
        Page<Ticket> tickets = ticketRepository.findAllByUserParticipant(userParticipant, pageable);
        return tickets.map(t -> modelMapper.map(t, TicketDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDTO> findAllByIssueDate(LocalDate issueDate, Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findAllByIssueDate(issueDate, pageable);
        return tickets.map(t -> modelMapper.map(t, TicketDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDTO> findAllByEventParticipated(EventDTO event, Pageable pageable) {
        Event eventParticipated = modelMapper.map(event, Event.class);
        Page<Ticket> tickets = ticketRepository.findAllByEventParticipated(eventParticipated, pageable);
        return tickets.map(t -> modelMapper.map(t, TicketDTO.class));
    }

    @Override
    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
        // TODO handle request for payment system (??)
        Ticket savedTicket = ticketRepository.save(ticket);
        return modelMapper.map(savedTicket, TicketDTO.class);
    }

    @Override
    @Transactional
    public void deleteTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + id + " not found"));
        ticketRepository.delete(ticket);
    }
}
