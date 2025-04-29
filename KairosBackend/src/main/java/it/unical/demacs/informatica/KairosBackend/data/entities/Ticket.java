package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="ticket")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @UuidGenerator
    @Column(name="id", nullable=false, unique=true, updatable=false,length=36)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable=false)
    private User userParticipant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable=false)
    private Event eventParticipated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable=false)
    private Sector sector;

    @Column(name = "issue_date", nullable=false)
    private LocalDate issueDate;

}
