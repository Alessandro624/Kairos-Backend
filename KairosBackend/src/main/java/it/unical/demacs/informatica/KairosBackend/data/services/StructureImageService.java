package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface StructureImageService {
    Optional<StructureImageDTO> findStructureImageById(UUID id);

    Optional<StructureImageDTO> findStructureImageByStructureId(UUID structureId);

    StructureImageDTO saveStructureImage(UUID structureId, StructureImageDTO structureImageDTO);

    StructureImageDTO uploadStructureImage(UUID structureId, MultipartFile file);

    void deleteStructureImage(UUID imageId);

    void deleteStructureImageByStructureId(UUID structureId);
}