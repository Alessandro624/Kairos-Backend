package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.services.StructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/structures", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class StructureController {
    private final StructureService structureService;

    private static final Logger logger = LoggerFactory.getLogger(StructureController.class);

    public StructureController(StructureService structureService) {
        this.structureService = structureService;
    }

    @GetMapping
    public List<Structure> getAllStructures() {
        logger.info("Richiesta per ottenere tutte le strutture");
        return structureService.getAllStructures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Structure> getStructure(@PathVariable UUID id)
    {
        logger.info("Richiesta per ottenere la struttura con ID: {}", id);
        return structureService.getStructureById(id)
                .map(structure -> {
                    logger.debug("Struttura trovata: {}", structure);
                    return ResponseEntity.ok(structure);
                })
                .orElseGet(() -> {
                    logger.warn("Struttura con ID {} non trovata", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping(consumes = "application/json")
    public Structure createStructure(@RequestBody Structure structure) {
        logger.info("Richiesta per creare una nuova struttura: {}", structure);
        return structureService.saveStructure(structure);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStructure(@PathVariable UUID id) {
        logger.info("Richiesta per eliminare la struttura con ID: {}", id);
        structureService.deleteStructure(id);
        return ResponseEntity.noContent().build();
    }
}
