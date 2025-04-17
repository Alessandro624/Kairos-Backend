package it.unical.demacs.informatica.KairosBackend.data.entities.embeddables;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address
{
    private String street;
    private String city;
    private String zipCode;
}

