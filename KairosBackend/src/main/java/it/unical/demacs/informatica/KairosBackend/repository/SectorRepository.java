package it.unical.demacs.informatica.KairosBackend.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SectorRepository extends JpaRepository<Sector, UUID> { }
