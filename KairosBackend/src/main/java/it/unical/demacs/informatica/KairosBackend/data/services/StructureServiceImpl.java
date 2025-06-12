package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Sector;
import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.repository.StructureRepository;
import it.unical.demacs.informatica.KairosBackend.data.repository.specifications.StructureSpecifications;
import it.unical.demacs.informatica.KairosBackend.dto.sector.SectorDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDetailsDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureFilterDTO;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StructureServiceImpl implements StructureService {
    private final StructureRepository structureRepository;
    private final ModelMapper modelMapper;

    @Override
    public StructureDetailsDTO findStructureDetailsById(UUID id) {
        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Structure with id: " + id + " not found."));

        return modelMapper.map(structure, StructureDetailsDTO.class);
    }

    @Override
    public Page<StructureDTO> findAllFiltered(StructureFilterDTO filterDTO, int page, int size, String sortBy, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Structure> structurePage = structureRepository.findAll(StructureSpecifications.filterStructure(filterDTO), pageable);

        List<StructureDTO> dtos = structurePage.getContent()
                .stream()
                .map(s -> modelMapper.map(s, StructureDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, structurePage.getTotalElements());
    }

    @Override
    public List<SectorDTO> findSectorsByStructureId(UUID id) {
        List<Sector> sectors = structureRepository.findSectorsByStructureId(id);
        return sectors.stream().map(sector -> modelMapper.map(sector, SectorDTO.class)).collect(Collectors.toList());
    }

    @Override
    public StructureDTO create(StructureCreateDTO dto) {
        Structure structure = modelMapper.map(dto, Structure.class);
        Structure savedStructure = structureRepository.save(structure);

        return modelMapper.map(savedStructure, StructureDTO.class);
    }

    @Override
    public void deleteById(UUID id) {
        if (!structureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Structure with id: " + id + " not found.");
        }
        structureRepository.deleteById(id);
    }

    @Override
    public StructureDetailsDTO findByName(String name) {
        Structure structure = structureRepository.findByName(name);

        return modelMapper.map(structure, StructureDetailsDTO.class);
    }
}
