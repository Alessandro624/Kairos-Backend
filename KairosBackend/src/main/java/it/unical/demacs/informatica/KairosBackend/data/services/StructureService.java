package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StructureService
{
    @Cacheable("allStructures")
    List<Structure> getAllStructures();

    Optional<Structure> getStructureById(UUID id);

    Structure saveStructure(Structure structure);

    void deleteStructure(UUID id);

    Optional<Structure> findByName(String name);
}