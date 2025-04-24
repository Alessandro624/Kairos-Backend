package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


// gab
// Evento(ID,titolo,descrizione,data_ora,max_partecipanti,organizzatore_id*,struttura_id*)

@Data
@Entity
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ID")
    private UUID id;

    @Size(min = 1, max = 50)
    @NonNull

    private String title;

    @Size(min = 1, max = 1000)
    @NonNull
    private String description;

    @NonNull
    private LocalDateTime time;

    @Size(min=1)
    private int maxParticipants;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ORGANIZER")
    private User organizer;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STRUCTURE")
    private Structure structure;

    // da capire
    // private List<User> participants = new ArrayList<>();
    //


    private List<EventImage> images;
}
