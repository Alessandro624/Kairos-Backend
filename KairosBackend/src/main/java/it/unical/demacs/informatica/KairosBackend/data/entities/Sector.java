package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
public class Sector
{
    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "STRUCTURE_ID")
    private Structure structure;

    @Column(name = "CAPACITY")
    private Integer capacity;
}
