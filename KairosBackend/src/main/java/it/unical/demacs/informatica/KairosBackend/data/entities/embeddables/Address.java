package it.unical.demacs.informatica.KairosBackend.data.entities.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address
{
    @Max(100)
    private String street;

    @Max(50)
    private String city;

    @Pattern(regexp = "\\d{5}")
    private String zipCode;
}
