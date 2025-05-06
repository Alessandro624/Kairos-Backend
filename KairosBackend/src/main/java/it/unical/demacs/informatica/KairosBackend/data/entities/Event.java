package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import it.unical.demacs.informatica.KairosBackend.listener.EntityAuditTrailListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event")
@EntityListeners(value = {AuditingEntityListener.class, EntityAuditTrailListener.class})
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

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return maxParticipants == event.maxParticipants && Objects.equals(id, event.id) && Objects.equals(title, event.title) && Objects.equals(description, event.description) && category == event.category && Objects.equals(dateTime, event.dateTime) && Objects.equals(organizer, event.organizer) && Objects.equals(structure, event.structure) && Objects.equals(sectors, event.sectors) && Objects.equals(images, event.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, dateTime, maxParticipants, organizer, structure, sectors, images);
    }
}
