package it.unical.demacs.informatica.KairosBackend.dto.sector;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class SectorDTO
{
    @NotNull(message = "Id cannot be null")
    private UUID id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters.")
    private String name;

    @Positive(message = "Capacity must be positive.")
    @NotNull(message = "Capacity cannot be null")
    private Integer capacity;
}
