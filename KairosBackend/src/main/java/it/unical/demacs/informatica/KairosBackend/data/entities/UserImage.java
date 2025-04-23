package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_image")
public class UserImage {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 36, nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
