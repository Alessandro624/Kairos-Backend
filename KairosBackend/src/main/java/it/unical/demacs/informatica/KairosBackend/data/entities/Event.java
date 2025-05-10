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
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event")
@EntityListeners(value = {AuditingEntityListener.class, EntityAuditTrailListener.class})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Event extends AuditableEntity {
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

    @Column(name = "visible", nullable = false)
    private boolean isVisible;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizer")
    private User organizer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_structure")
    private Structure structure;

    @OneToMany(mappedBy = "event")
    private List<EventSector> sectors;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventImage> images;
}
