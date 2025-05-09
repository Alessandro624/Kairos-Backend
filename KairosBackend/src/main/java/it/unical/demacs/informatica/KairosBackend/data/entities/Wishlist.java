package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import it.unical.demacs.informatica.KairosBackend.listener.EntityAuditTrailListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

//AUDITING: note that 'creator' is different
//one is used for business logic, the other one is used for auditing.
//'createdBy' is thought as something used internally

@Entity
@Table(name="wishlist")
@Data
@EntityListeners(value = {AuditingEntityListener.class, EntityAuditTrailListener.class})
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Wishlist extends AuditableEntity {
    @Id
    @UuidGenerator
    @Column(name="id", columnDefinition = "uuid", nullable=false, unique=true, updatable=false,length=36)
    private UUID id;

    @Column(nullable=false,length=50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private WishlistScope scope;

    //TODO add list of created wishlists in User entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable=false)
    private User creator;

    @ManyToMany()
    @JoinTable(
            name = "wishlist_event",
            joinColumns = @JoinColumn(name= "wishlist_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name= "event_id", nullable = false)
    )
    private List<Event> wishedEvents;

    //TODO change into two one-to-many mapping
    @ManyToMany
    @JoinTable(
            name="wishlist_user",
            joinColumns = @JoinColumn(name="wishlist_id", nullable=false),
            inverseJoinColumns = @JoinColumn(name="user_id", nullable=false)
    )
    private List<User> sharedUsers;
}

