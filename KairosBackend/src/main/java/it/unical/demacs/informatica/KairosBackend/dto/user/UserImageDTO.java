package it.unical.demacs.informatica.KairosBackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserImageDTO {
    @NotNull(message = "User image id cannot be null")
    private UUID id;

    @NotBlank(message = "Photo url cannot be blank")
    private String photoUrl;
}
