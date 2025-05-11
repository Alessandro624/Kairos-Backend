package it.unical.demacs.informatica.KairosBackend.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewUpdateDTO {

    @Size(min = 1, max = 5, message = "Rating must be between 1 and 5")
    @NotNull(message = "Rating cannot be blank")
    private int rating;

    @Size(min = 2, max = 500, message = "Used not enough or too much characters for the comment")
    @NotBlank(message = "Comment cannot be blank")
    private String comment;
}
