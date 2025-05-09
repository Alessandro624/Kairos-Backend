package it.unical.demacs.informatica.KairosBackend.data.entities;

import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import it.unical.demacs.informatica.KairosBackend.listener.EntityAuditTrailListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "structure")
@EntityListeners(EntityAuditTrailListener.class)
public class Structure extends AuditableEntity
{
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, unique = true, updatable = false, length = 36)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Embedded
    @AttributeOverrides ({
            @AttributeOverride(name = "street", column = @Column(name = "address_street", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "address_city", nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "address_zipcode", nullable = false))
    })
    private Address address;

    @NotNull(message = "Country cannot be null")
    private String country;

    @OneToOne(mappedBy = "structure", cascade = CascadeType.ALL, orphanRemoval = true)
    private StructureImage structureImage;

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sector> sectors;
}
