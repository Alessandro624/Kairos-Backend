package it.unical.demacs.informatica.KairosBackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO can be used for review, just image and username of the User
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private String username;

    private UserImageDTO profileImage;
}
