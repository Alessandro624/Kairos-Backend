package it.unical.demacs.informatica.KairosBackend.dto.structure;

import jakarta.validation.constraints.*;
import lombok.*;

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
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 150, message = "Street must be at most 150 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @NotBlank(message = "Zip code cannot be blank")
    @Size(max = 20, message = "Zip code must be at most 20 characters")
    private String zipCode;

    private StructureImageDTO image;
}
