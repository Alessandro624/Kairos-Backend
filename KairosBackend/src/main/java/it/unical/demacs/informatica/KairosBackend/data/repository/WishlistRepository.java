package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WishlistRepository extends ListCrudRepository<Wishlist, UUID>, PagingAndSortingRepository<Wishlist, UUID>, JpaSpecificationExecutor<Wishlist> {

    Integer countByCreator_Id(UUID creatorId);

    Page<Wishlist> findAllByCreator_Id(UUID creatorID, Pageable pageable);

    Page<Wishlist> findAllByNameContaining(String name, Pageable pageable);

    @Query("SELECT w from Wishlist w JOIN w.sharedUsers wu WHERE wu.id = :userId")
    Page<Wishlist> findAllBySharedUser(@Param("userId") UUID user, Pageable pageable);
}
