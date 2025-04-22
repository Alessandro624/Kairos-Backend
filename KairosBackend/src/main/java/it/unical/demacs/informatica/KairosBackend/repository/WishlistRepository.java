package it.unical.demacs.informatica.KairosBackend.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface WishlistRepository extends ListCrudRepository<Wishlist, UUID>, PagingAndSortingRepository<Wishlist, UUID> {

    Integer countByCreator_Id(UUID creatorId);

    Page<Wishlist> findByCreator_Id(UUID creatorID, Pageable pageable);
    Page<Wishlist> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT w from Wishlist w JOIN w.sharedUsers wu WHERE wu.id = :userId")
    Page<Wishlist> findBySharedUser(@Param("userId") UUID user, Pageable pageable);
}
