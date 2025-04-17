package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Data
@Entity
public class Structure
{
    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "ADDRESS_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "ADDRESS_CITY")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "ADDRESS_ZIP"))
    })
    private Address address;

    @OneToMany(mappedBy = "structure")
    Set<EventSector> sectors;
}
