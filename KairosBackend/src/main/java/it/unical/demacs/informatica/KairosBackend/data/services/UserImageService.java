package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.user.UserImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface UserImageService {
    Optional<UserImageDTO> findUserImageById(UUID id);

    Optional<UserImageDTO> findUserImageByUserId(UUID userId);

    UserImageDTO saveUserImage(UUID userId, UserImageDTO userImageDTO);

    UserImageDTO uploadUserImage(UUID userId, MultipartFile file);

    void deleteUserImage(UUID imageId);

    void deleteUserImageByUserId(UUID userId);
}
