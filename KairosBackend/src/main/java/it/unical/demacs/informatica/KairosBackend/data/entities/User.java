package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

// TODO add interface UserDetails for Spring Security
// TODO think of using Set instead of List

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(name = "id", length = 36, nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserImage profileImage;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists;

    @ManyToMany(mappedBy = "sharedUsers")
    private List<Wishlist> sharedWishlists;

    // TODO tickets mapping (only role == PARTICIPANT)
    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Ticket> tickets;

    // TODO events mapping (only role == ORGANIZER), needs to be a @ManyToOne on event side
    // @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Event> events;

    // TODO implement UserDetails methods
}
