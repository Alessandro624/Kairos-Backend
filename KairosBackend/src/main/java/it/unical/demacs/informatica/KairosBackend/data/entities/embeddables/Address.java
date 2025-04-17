package it.unical.demacs.informatica.KairosBackend.data.entities.embeddables;

import jakarta.persistence.Embeddable;
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
    private String street;
    private String city;
    private String zipCode;
}
