package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
public class EventSector
{
    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "SECTOR_ID")
    private Sector sector;

    @Column(name = "PRICE")
    private float price;
}
