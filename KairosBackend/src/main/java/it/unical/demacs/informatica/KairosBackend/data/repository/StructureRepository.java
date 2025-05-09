package it.unical.demacs.informatica.KairosBackend.data.repository;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.dto.sector.SectorDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StructureRepository extends ListCrudRepository<Structure, UUID>, PagingAndSortingRepository<Structure, UUID>
{
    @Query("SELECT s.sectors FROM Structure s WHERE s.id = :structureId")
    List<SectorDTO> findSectorsByStructureId(@Param("structureId") UUID structureId);
    StructureDTO findByName(String name);
}
