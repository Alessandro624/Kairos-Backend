package it.unical.demacs.informatica.KairosBackend.dto.events.eventimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventImageCreateDTO {

    @NotNull(message = "preference cannot be null.")
    @Positive(message = "preference must be positive.")
    private int preference;

    @NotBlank(message = "photoUrl cannot be blank.")
    @Pattern(regexp = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
            message = "photoUrl must be a valid link.")
    private String photoUrl;
}
