package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.WishlistScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class WishlistCreateDTO {

    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50.")
    @NotBlank(message = "Name must be defined")
    private String name;

    //when created, the scope is always PRIVATE.

    @NotNull(message = "Creator must be defined")
    private UUID creator;
}
