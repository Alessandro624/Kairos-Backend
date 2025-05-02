package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="wishlist")
@Data
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
public class Wishlist {
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

    //TODO add list of wishlists in Event entity
    @ManyToMany()
    @JoinTable(
            name = "wishlist_event",
            joinColumns = @JoinColumn(name= "wishlist_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name= "event_id", nullable = false)
    )
    private List<Event> wishedEvents;

    @ManyToMany
    @JoinTable(
            name="wishlist_user",
            joinColumns = @JoinColumn(name="wishlist_id", nullable=false),
            inverseJoinColumns = @JoinColumn(name="user_id", nullable=false)
    )
    private List<User> sharedUsers;

    //FIXME maybe a superclass or an interface containing these methods (as well as annotations)
    //AUDITING: note that 'creator' is different
    //one is used for business logic, the other one is used for auditing.
    //'createdBy' is thought as something used internally
    @CreatedBy
    @Column(name = "created_by", columnDefinition="uuid", updatable = false, nullable=false, length = 36)
    private UUID createdBy;

    //creationDate can be used for both auditing and business logic instead
    @CreatedDate
    @Column(name = "creation_date", nullable=false, updatable = false)
    private LocalDateTime creationDate;

    //still useful (maybe ADMIN can modify the wishlist)
    @LastModifiedBy
    @Column(name = "last_modified_by", nullable=false)
    private UUID lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;
}

