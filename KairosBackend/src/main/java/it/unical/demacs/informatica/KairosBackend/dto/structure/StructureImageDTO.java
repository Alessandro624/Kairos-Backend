package it.unical.demacs.informatica.KairosBackend.dto.structure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StructureImageDTO
{
    @NotNull(message = "Structure image ID cannot be null")
    private UUID id;

    @NotBlank(message = "Photo URL cannot be blank")
    private String photoUrl;
}
