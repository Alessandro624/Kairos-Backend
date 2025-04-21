package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
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
    @Column(name = "ID", nullable = false, unique = true, updatable = false, length = 36)
    private UUID id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Embedded
    @AttributeOverrides ({
            @AttributeOverride(name = "street", column = @Column(name = "ADDRESS_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "ADDRESS_CITY")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "ADDRESS_ZIPCODE"))
    })
    private Address address;

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sector> sectors;
}
