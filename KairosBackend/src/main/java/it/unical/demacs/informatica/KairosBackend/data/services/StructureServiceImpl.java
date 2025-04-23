package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.repository.StructureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StructureServiceImpl implements StructureService {
    private final StructureRepository structureRepository;

    public StructureServiceImpl(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    @Override
    public List<Structure> getAllStructures() {
        return structureRepository.findAll();
    }

    @Override
    public Optional<Structure> getStructureById(UUID id) {
        return structureRepository.findById(id);
    }

    @Override
    public Structure saveStructure(Structure structure) {
        return structureRepository.save(structure);
    }

    @Override
    public void deleteStructure(UUID id) {
        structureRepository.deleteById(id);
    }
}
