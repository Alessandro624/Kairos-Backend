package it.unical.demacs.informatica.KairosBackend.dto.structure;

import it.unical.demacs.informatica.KairosBackend.data.entities.Sector;
import it.unical.demacs.informatica.KairosBackend.data.entities.StructureImage;
import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class StructureCreateDTO
{
    @NotNull(message = "Id cannot be null")
    private UUID id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters.")
    private String name;

    @NotNull(message = "Address cannot be null")
    private Address address;

    @NotNull(message = "Country cannot be null")
    private String country;

    @NotNull(message = "StructureImage cannot be null")
    private StructureImage structureImage;
}
