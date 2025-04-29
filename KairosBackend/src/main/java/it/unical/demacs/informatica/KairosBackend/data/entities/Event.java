package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(name = "id", length = 36, nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
    private UUID id;

    @Size(min = 1, max = 100)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(min = 1, max = 1000)
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    @Size(min=1)
    @Column(name = "maxparticipants", nullable = false)
    private int maxParticipants;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizer")
    private User organizer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_structure")
    private Structure structure;

    @OneToMany(mappedBy = "eventsector")
    private List<EventSector> sectors;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventImage> images;
}
