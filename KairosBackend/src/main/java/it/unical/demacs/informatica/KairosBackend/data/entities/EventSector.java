package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventsector")
public class EventSector
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ID", nullable = false, unique = true, updatable = false, length = 36)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTOR_ID")
    private Sector sector;

    @Column(name = "PRICE", precision = 10, scale = 2)
    private BigDecimal price;
}
