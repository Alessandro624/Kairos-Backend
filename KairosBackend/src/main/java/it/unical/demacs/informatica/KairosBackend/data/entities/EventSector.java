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
public class EventSector
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true, updatable = false, length = 36)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}
