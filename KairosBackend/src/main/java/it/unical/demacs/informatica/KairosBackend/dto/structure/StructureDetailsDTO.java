package it.unical.demacs.informatica.KairosBackend.dto.structure;

import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StructureDetailsDTO
{
    @NotNull(message = "Id cannot be null")
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters.")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    private Address address;

    @NotNull(message = "Country cannot be null")
    private String country;

    @NotBlank(message = "Image cannot be blank")
    private StructureImageDTO image;

    @NotBlank(message = "Sectors cannot be blank")
    private List<UUID> sectors;
}
