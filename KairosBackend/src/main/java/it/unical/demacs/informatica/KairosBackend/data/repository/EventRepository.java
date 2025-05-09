package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>, PagingAndSortingRepository<Event, UUID>, JpaSpecificationExecutor<Event> {
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Event e " +
            "WHERE lower(e.title) = lower(:title) AND e.dateTime = :data " +
            "AND e.structure.id = :structure AND e.organizer.id = :organizer")
    boolean existsEvent(@Param("title") String title, @Param("data") LocalDateTime data,
                        @Param("structure") UUID structureId, @Param("organizer") UUID organizerId);


    @Query("SELECT e FROM Event e " +
            "WHERE e.structure.address.city = :city OR e.structure.address.zipCode = :zip " +
            "AND e.isVisible = True AND e.dateTime >= :fromDate " +
            "ORDER BY e.dateTime ASC")
    Page<Event> findEventsNearLocationStartingFrom(@Param("city") String city, @Param("zip") String zip,
                                                   @Param("fromDate") LocalDateTime now, Pageable pageable);

    // posso rimuoverli?
    Page<Event> findAllByOrganizerAndVisibleTrueOrderByDateTimeAsc(User user, Pageable pageable);
    Page<Event> findAllByDateTimeBetweenAndVisibleTrueOrderByDateTimeAsc(LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<Event> findAllByStructureAndVisibleTrueOrderByDateTimeAsc(Structure structure, Pageable pageable);
    Page<Event> findAllByCategoryAndVisibleTrueOrderByDateTimeAsc(Category category, Pageable pageable);
}
