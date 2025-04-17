package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Sector
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STRUCTURE_ID")
    private Structure structure;

    @Column(name = "CAPACITY")
    private Integer capacity;
}
