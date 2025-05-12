package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Review;
import it.unical.demacs.informatica.KairosBackend.data.repository.ReviewRepository;
import it.unical.demacs.informatica.KairosBackend.dto.events.EventDTO;
import it.unical.demacs.informatica.KairosBackend.dto.review.ReviewDTO;
import it.unical.demacs.informatica.KairosBackend.dto.review.ReviewUpdateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findById(UUID id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(reviewDTO -> modelMapper.map(reviewDTO, ReviewDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAllByUserParticipant(UserDTO user, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByTicketReferenceUserParticipantId(user.getId(), pageable);
        return reviews.map(r -> modelMapper.map(r, ReviewDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAllByEventParticipated(EventDTO eventDTO, Pageable pageable) {
        Event event = modelMapper.map(eventDTO, Event.class);
        Page<Review> reviews = reviewRepository.findAllByEventParticipated(event, pageable);
        return reviews.map(r -> modelMapper.map(r, ReviewDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findAllByRating(Integer rating, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByRating(rating, pageable);
        return reviews.map(r -> modelMapper.map(r, ReviewDTO.class));
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        // TODO check for blacklisted words (??)
        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewDTO.class);
    }

    @Override
    public void deleteReview(UUID id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        reviewRepository.delete(review);
    }

    @Override
    public ReviewDTO updateReview(UUID id, ReviewUpdateDTO reviewDTO) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        Review updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewDTO.class);
    }
}
