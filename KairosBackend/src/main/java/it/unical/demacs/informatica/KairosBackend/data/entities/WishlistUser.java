package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "wishlist_user",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "wishlist_id", "user_id" }) })
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
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
