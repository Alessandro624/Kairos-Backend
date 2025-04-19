package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends ListCrudRepository<Wishlist, UUID> {
    List<Wishlist> findByOwnerId(UUID ownerId);

    List<Wishlist> findByNameContaining(String name);

    @Query("from Wishlist w where w.scope = 'SHARED' and :user in w.sharedUsers")
    List<Wishlist> findAllBySharedUsers(@Param("user") User user);
}
