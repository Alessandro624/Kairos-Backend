package it.unical.demacs.informatica.KairosBackend.dto.user;

import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Provider;
import jakarta.validation.constraints.Email;
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
public class UserDTO {
    @NotNull(message = "User id cannot be null")
    private UUID id;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String phoneNumber;

    // @NotNull(message = "Role cannot be null")
    // private UserRole role;

    @NotNull(message = "Authentication provider cannot be null")
    private Provider provider;

    private UserImageDTO profileImage;
}
