package it.unical.demacs.informatica.KairosBackend.utility.listener;

import lombok.Getter;

import java.util.UUID;

@Getter
public class getStructureByIdEvent
{
    private final UUID id;

    public getStructureByIdEvent(UUID id)
    {
        this.id = id;
    }
}
