package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.User;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructurePreviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StructureRepository extends JpaRepository<Structure, UUID>
{
    Optional<Structure> findByUsername(String username);
}
