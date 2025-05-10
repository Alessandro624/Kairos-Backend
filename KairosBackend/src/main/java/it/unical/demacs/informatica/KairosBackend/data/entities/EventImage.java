package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event_image", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_event", "preference"})
})
public class EventImage {
    @Id
    @GeneratedValue
    @Column(name = "id", length = 36, nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    // this column refers to the organizer's preferred order of viewing the photos
    @Column(name = "preference", nullable = false)
    private int preference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event", nullable = false)
    private Event event;
}
