package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="wishlist")
@Data
@NoArgsConstructor
public class Wishlist {
    //nota: i controlli (null, min, max...) a quanto pare converrebbe inserirli direttamente nei DTO.
    @Id
    @UuidGenerator
    @Column(name="id", nullable=false, unique=true, updatable=false,length=36)
    private UUID id;

    @Column(nullable=false,length=50)
    private String name;

    @Column(name = "creation_date", nullable=false)
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private WishlistScope scope;

    //TODO add list of created wishlists in User entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable=false)
    private User creator;

    //TODO add list of wishlists in Event entity
    @ManyToMany()
    @JoinTable(
            name = "wishlist_event",
            joinColumns = @JoinColumn(name= "wishlist_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name= "event_id", nullable = false)
    )
    private List<Event> wishedEvents;

    //TODO change @OneToMany in @ManyToMany in User entity
    @ManyToMany
    @JoinTable(
            name="wishlist_user",
            joinColumns = @JoinColumn(name="wishlist_id", nullable=false),
            inverseJoinColumns = @JoinColumn(name="user_id", nullable=false)
    )
    private List<User> sharedUsers;
}

