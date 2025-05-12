package it.unical.demacs.informatica.KairosBackend.dto.structure;

import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
public class StructureFilterDTO
{
    @NotNull(message = "User id cannot be null")
    private UUID id;

    private String name;
    private Address address;
    private String country;
}
