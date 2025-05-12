package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "wishlist_user")
public class WishlistUser {
    @Id
    @UuidGenerator
    @Column(name="id", columnDefinition = "uuid", nullable=false, unique=true, updatable=false,length=36)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    //FIXME set @OneToMany annotation in User class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean accepted;
}
