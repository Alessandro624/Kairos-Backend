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

    Optional<Review> findByTicketReference(Ticket ticketReference);
    Page<Review> findAllByTicketReferenceUserParticipantId(UUID id, Pageable pageable);
    Page<Review> findAllByEventParticipated(Event event, Pageable pageable);
    Page<Review> findAllByRating(Integer rating, Pageable pageable);
    Page<Review> findAllByRatingAndEventParticipated(int rating, Event event, Pageable pageable);

}
