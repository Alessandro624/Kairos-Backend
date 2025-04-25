package it.unical.demacs.informatica.KairosBackend.data.services;

import it.unical.demacs.informatica.KairosBackend.dto.user.UserImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface UserImageService {
    Optional<UserImageDTO> findUserImageById(UUID id);

    Optional<UserImageDTO> findUserImageByUserId(UUID userId);

    // TODO throws UserNotFoundException if user not exists
    UserImageDTO saveUserImage(UUID userId, UserImageDTO userImageDTO);

    // TODO throws UserNotFoundException if user not exists or an exception for file errors
    UserImageDTO uploadUserImage(UUID userId, MultipartFile file);

    // TODO throws UserImageNotFoundException
    void deleteUserImage(UUID imageId);

    // TODO throws UserNotFoundException
    void deleteUserImageByUserId(UUID userId);
}
