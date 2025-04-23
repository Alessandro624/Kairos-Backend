package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

// TODO add interface UserDetails for Spring Security

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
    @NotNull(message = "Id cannot be null")
    @Size(min = 36, max = 36)
    @Column(name = "id", updatable = false, unique = true, columnDefinition = "UUID")
    private UUID id;

    @NotBlank(message = "First name cannot be blank")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email", nullable = false)
    private String email;

    // TODO password validation with custom annotation
    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password", nullable = false)
    private String password;

    // TODO phone number validation
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    // TODO role mapping -> it can be handled as list of roles ?
    // @Enumerated(EnumType.STRING)
    // @NotNull(message = "Role cannot be null")
    // @Column(name = "role", nullable = false)
    // private UserRole role;

    @NotNull(message = "Authentication provider cannot be null")
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

    // TODO events mapping (only role == ORGANIZER)
    // @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Event> events;

    // TODO implement UserDetails methods
}
