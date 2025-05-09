package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.sector.SectorDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDetailsDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface StructureService
{
    @Cacheable("structures")
    Page<StructureDTO> findAll(int page, int size, String sortBy, Sort.Direction direction);

    StructureDetailsDTO findStructureDetailsById(UUID id);
    List<SectorDTO> findSectorsByStructureId(UUID structureId);
    StructureDTO create(StructureCreateDTO dto);
    void deleteById(UUID id);
    StructureDetailsDTO findByName(String existingStructureName);
}
