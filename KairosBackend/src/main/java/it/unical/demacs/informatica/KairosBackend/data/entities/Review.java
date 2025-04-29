package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name="review")
@Data
@NoArgsConstructor

public class Review {

    @Id
    @UuidGenerator
    @Column(name="id", nullable=false, unique=true, updatable=false,length=36)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable=false)
    private Ticket ticketReference;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable=false)
    private Event eventParticipated;

    @Size(min = 1, max= 5)
    @Column(name = "rating", nullable=false)
    private Integer rating;

    @Column(name = "comment", nullable = false, length = 500)
    private String comment;
}
