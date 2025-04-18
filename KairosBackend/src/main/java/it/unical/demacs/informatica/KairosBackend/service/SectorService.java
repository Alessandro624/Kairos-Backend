package it.unical.demacs.informatica.KairosBackend.service;

import it.unical.demacs.informatica.KairosBackend.data.entities.Sector;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectorService
{
    List<Sector> getAllSectors();
    Optional<Sector> getSectorById(UUID id);
    Sector saveSector(Sector sector);
    void deleteSector(UUID id);
}
