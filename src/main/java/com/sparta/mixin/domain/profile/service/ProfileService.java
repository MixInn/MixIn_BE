package com.sparta.mixin.domain.profile.service;

import com.sparta.mixin.domain.profile.dto.ProfileRequestDto;
import com.sparta.mixin.domain.profile.dto.ProfileResponseDto;
import com.sparta.mixin.domain.profile.entity.Profile;
import com.sparta.mixin.domain.profile.entity.ProfileRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.entity.UserRepository;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Value("${profile.image-dir}")
    private String profileImageDirectory;

    @Transactional
    public void createProfile(ProfileRequestDto requestDto, User user) {
        Profile profile = new Profile(
                requestDto.getParticipationType(),
                requestDto.getPersonality(),
                requestDto.getInterest(),
                requestDto.getValueSystem(),
                null,
                requestDto.getShortIntro()
        );
        profileRepository.save(profile);
        user.updateProfile(profile);
        userRepository.save(user);
    }

    @Transactional
    public void uploadProfileImage(MultipartFile file, User user) {
        Profile profile = user.getProfile();
        validateProfileImage(file);
        String filePath = saveProfileImage(file);
        profile.addPofileImage(filePath);
        profileRepository.save(profile);
    }

    public ProfileResponseDto getProfile(User user) {
        Profile profile = user.getProfile();
        return new ProfileResponseDto(
                profile.getId(),
                user.getName(),
                profile.getParticipationType(),
                profile.getPersonality(),
                profile.getInterest(),
                profile.getValueSystem(),
                profile.getProfileImage(),
                profile.getShortIntro()
        );
    }

    @Transactional
    public void deleteProfileImage(User user) {
        Profile profile = user.getProfile();
        deleteFile(profile.getProfileImage());
        profile.addPofileImage(null);
        profileRepository.save(profile);
    }

    @Transactional
    public void updateProfileImage(MultipartFile file, User user) {
        Profile profile = user.getProfile();

        if (profile.getProfileImage() != null) {
            deleteFile(profile.getProfileImage());
        }

        String filePath = saveProfileImage(file);
        profile.addPofileImage(filePath);
        profileRepository.save(profile);
    }

    private void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    private void validateProfileImage(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String fileExtension = getFileExtension(filename).toLowerCase();
        long fileSize = file.getSize();

        if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png")) {
            if (fileSize > 10 * 1024 * 1024) {
                throw new CustomException(ErrorCode.NOT_ALLOW_IMAGE_SIZE);
            }
        } else {
            throw new CustomException(ErrorCode.NOT_ALLOW_FORMAT);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new CustomException(ErrorCode.INCORRECT_FILE_NAME);
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            throw new CustomException(ErrorCode.INCORRECT_EXTENSION);
        }
        return filename.substring(dotIndex + 1);
    }

    private String saveProfileImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(profileImageDirectory + fileName);

            File directory = new File(profileImageDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return profileImageDirectory + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

}
