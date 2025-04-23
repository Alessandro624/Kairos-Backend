package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.UserImageDTO;

import java.util.Optional;
import java.util.UUID;

public interface UserImageService {
    Optional<UserImageDTO> findUserImageById(UUID id);

    Optional<UserImageDTO> findUserImageByUserId(UUID userId);

    UserImageDTO saveUserImage(UUID userId, UserImageDTO userImageDTO);

    void deleteUserImage(UUID imageId);

    void deleteUserImageByUserId(UUID userId);
}
