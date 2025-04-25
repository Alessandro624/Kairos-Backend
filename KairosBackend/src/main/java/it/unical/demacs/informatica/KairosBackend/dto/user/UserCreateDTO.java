package it.unical.demacs.informatica.KairosBackend.dto.user;

import it.unical.demacs.informatica.KairosBackend.dto.annotations.Password;
import it.unical.demacs.informatica.KairosBackend.dto.annotations.PhoneNumber;
import it.unical.demacs.informatica.KairosBackend.dto.annotations.Username;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    @Username
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Password(max = 15, allowedSymbols = "@#$%^&+=!*()-_")
    private String password;

    @PhoneNumber
    private String phoneNumber;
}
