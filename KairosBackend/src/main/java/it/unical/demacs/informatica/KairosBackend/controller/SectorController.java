package it.unical.demacs.informatica.KairosBackend.controller;

import it.unical.demacs.informatica.KairosBackend.data.entities.Sector;
import it.unical.demacs.informatica.KairosBackend.data.services.SectorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/structures", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class SectorController
{
    private final SectorServiceImpl sectorService;

    public SectorController(SectorServiceImpl sectorService)
    {
        this.sectorService = sectorService;
    }

    @GetMapping("/sectors")
    public ResponseEntity<List<Sector>> getAllSectors()
    {
        List<Sector> sectors = sectorService.getAllSectors();
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }

    @GetMapping("/sectors/{id}")
    public ResponseEntity<Sector> getSectorById(@PathVariable UUID id)
    {
        Optional<Sector> sector = sectorService.getSectorById(id);
        return sector.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/sectors")
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector)
    {
        Sector savedSector = sectorService.saveSector(sector);
        return new ResponseEntity<>(savedSector, HttpStatus.CREATED);
    }

    @DeleteMapping("/sectors/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable UUID id)
    {
        Optional<Sector> sector = sectorService.getSectorById(id);
        if (sector.isPresent())
        {
            sectorService.deleteSector(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
