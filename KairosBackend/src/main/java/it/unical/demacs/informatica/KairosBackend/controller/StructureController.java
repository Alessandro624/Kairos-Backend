package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.dto.StructureDTO;
import it.unical.demacs.informatica.KairosBackend.repository.StructureRepository;
import it.unical.demacs.informatica.KairosBackend.service.StructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/structures",  produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080" )
public class StructureController
{
    private final StructureService structureService;

    public StructureController(StructureService structureService)
    {
        this.structureService = structureService;
    }

    @GetMapping
    public List<Structure> getAllStructures()
    {
        return structureService.getAllStructures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Structure> getStructure(@PathVariable UUID id)
    {
        return structureService.getStructureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public Structure createStructure(@RequestBody Structure structure)
    {
        return structureService.saveStructure(structure);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStructure(@PathVariable UUID id)
    {
        structureService.deleteStructure(id);
        return ResponseEntity.noContent().build();
    }
}
