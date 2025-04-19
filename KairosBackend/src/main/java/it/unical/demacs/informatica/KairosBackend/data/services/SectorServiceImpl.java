package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.data.entities.Sector;
import it.unical.demacs.informatica.KairosBackend.data.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectorServiceImpl implements SectorService {
    private final SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    @Override
    public List<Sector> getAllSectors() {
        return sectorRepository.findAll();
    }

    @Override
    public Optional<Sector> getSectorById(UUID id) {
        return sectorRepository.findById(id);
    }

    @Override
    public Sector saveSector(Sector sector) {
        return sectorRepository.save(sector);
    }

    @Override
    public void deleteSector(UUID id) {
        sectorRepository.deleteById(id);
    }
}
