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

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface StructureRepository extends
        ListCrudRepository<Structure, UUID>,
        PagingAndSortingRepository<Structure, UUID>,
        JpaSpecificationExecutor<Structure> {

    @Query("SELECT sector " +
            "FROM StructureSector ss, Sector sector " +
            "WHERE ss.structure.id = :structureId " +
            "AND sector.id = ss.sector")
    List<SectorDTO> findSectorsByStructureId(@Param("structureId") UUID structureId);

    @Query("SELECT ss.capacity " +
            "FROM StructureSector ss " +
            "WHERE ss.structure.id = :structureId " +
            "AND ss.sector.id = :sectorId")
    Integer findCapacityByStructureAndSector(@Param("sectorId") UUID sectorId);

    StructureDTO findByName(String name);
}
