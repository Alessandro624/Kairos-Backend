package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Structure
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Embedded
    @AttributeOverrides ({
            @AttributeOverride(name = "street", column = @Column(name = "ADDRESS_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "ADDRESS_CITY")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "ADDRESS_ZIP"))
    })
    private Address address;

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Sector> sectors;
}
