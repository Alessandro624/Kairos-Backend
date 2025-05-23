package it.unical.demacs.informatica.KairosBackend.dto.user;

import it.unical.demacs.informatica.KairosBackend.dto.annotations.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordUpdateDTO {
    @NotBlank(message = "OldPassword cannot be blank")
    private String oldPassword;

    @NotBlank(message = "NewPassword cannot be blank")
    @Password(max = 15, allowedSymbols = "@#$%^&+=!*()-_")
    private String newPassword;
}
