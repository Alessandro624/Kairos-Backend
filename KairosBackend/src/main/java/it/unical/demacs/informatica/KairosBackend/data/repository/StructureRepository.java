package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StructureRepository extends JpaRepository<Structure, UUID> {
}
