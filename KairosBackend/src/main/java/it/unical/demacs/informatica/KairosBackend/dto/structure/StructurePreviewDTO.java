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
public class StructurePreviewDTO
{
    @NotNull(message = "Id cannot be null")
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters.")
    private String name;

    @NotBlank(message = "Street cannot be blank")
    @Size(min = 1, max = 150, message = "Street must be between 1 and 100 characters.")
    private String street;

    private StructureImageDTO image;
}