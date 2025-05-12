package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.review.ReviewDTO;
import it.unical.demacs.informatica.KairosBackend.dto.review.ReviewUpdateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ReviewService {

    Optional<ReviewDTO> findById(UUID id);
    Page<ReviewDTO> findAllByUserParticipant(UserDTO user, Pageable pageable);
    Page<ReviewDTO> findAllByEventParticipated(EventDTO eventDTO, Pageable pageable);
    Page<ReviewDTO> findAllByRating(Integer rating, Pageable pageable);

    ReviewDTO createReview(ReviewDTO reviewDTO);
    void deleteReview(UUID id);
    ReviewDTO updateReview(UUID id, ReviewUpdateDTO reviewDTO);

}
