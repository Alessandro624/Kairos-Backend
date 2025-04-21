package it.unical.demacs.informatica.KairosBackend.service;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.dto.StructureDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StructureService
{
    List<Structure> getAllStructures();
    Optional<Structure> getStructureById(UUID id);
    Structure saveStructure(Structure structure);
    void deleteStructure(UUID id);
}
