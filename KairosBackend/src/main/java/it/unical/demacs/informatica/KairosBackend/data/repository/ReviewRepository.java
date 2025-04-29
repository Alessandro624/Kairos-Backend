package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Review;
import it.unical.demacs.informatica.KairosBackend.data.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findAllByEventParticipated(Event event, Pageable pageable);
    Optional<Review> findByTicketReference(Ticket ticketReference);
    Page<Review> findAllByRatingAndEventParticipated(int rating, Event event, Pageable pageable);

}
