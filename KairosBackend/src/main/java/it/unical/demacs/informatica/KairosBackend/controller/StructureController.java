package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.data.services.StructureService;
import it.unical.demacs.informatica.KairosBackend.dto.sector.SectorDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDetailsDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/structures")
@CrossOrigin(origins = "http://localhost:8080")
public class StructureController
{
    private final StructureService structureService;

    public StructureController(StructureService structureService)
    {
        this.structureService = structureService;
    }

    @GetMapping
    public ResponseEntity<Page<StructureDTO>> getAllPreviewStructures (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        return ResponseEntity.ok(structureService.findAll(page, size, "id", Sort.Direction.DESC));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructureDetailsDTO> getStructureDetails(@PathVariable UUID id)
    {
        return ResponseEntity.ok(structureService.findStructureDetailsById(id));
    }

    @GetMapping("/{id}/sectors")
    public ResponseEntity<List<SectorDTO>> getSectorsByStructureId(@PathVariable UUID id)
    {
        return ResponseEntity.ok(structureService.findSectorsByStructureId(id));
    }

    @PostMapping
    public ResponseEntity<StructureDTO> createStructure(@Valid @RequestBody StructureCreateDTO structureDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(structureService.create(structureDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStructure(@PathVariable UUID id)
    {
        structureService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}