package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Column(name = "ID", nullable = false, unique = true, updatable = false, length = 36)
    private UUID id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STRUCTURE_ID", nullable = false)
    private Structure structure;

    @Min(0)
    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;
}
