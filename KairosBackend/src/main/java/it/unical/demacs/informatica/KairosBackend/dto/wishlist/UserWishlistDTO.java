package it.unical.demacs.informatica.KairosBackend.dto.wishlist;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//USER DATA USED IN WishlistDTO class
@Data
public class UserWishlistDTO {
    @NotBlank(message = "Username cannot be blank")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // TODO add user image
    // private UserImage profileImage;
}
