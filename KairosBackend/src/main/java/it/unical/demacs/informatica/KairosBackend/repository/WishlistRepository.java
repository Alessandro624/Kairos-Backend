package it.unical.demacs.informatica.KairosBackend.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.data.entities.Wishlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WishlistRepository extends ListCrudRepository<Wishlist, UUID>{
    List<Wishlist> findByCreatorId(UUID creatorId);
    List<Wishlist> findByNameContaining(String name);

    List<Wishlist> findBySharedUsers_id(UUID userId);
}
