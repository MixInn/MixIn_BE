package com.sparta.mixin.domain.profile.service;

import com.sparta.mixin.domain.image.ImageService;
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

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ImageService imageService;

    @Value("${profile.image-dir}")
    private String uploadDirectory;

    @Transactional
    public void createProfile(ProfileRequestDto requestDto, User user) {

        Profile profile = new Profile(
                user,
                requestDto.getParticipationType(),
                requestDto.getPersonality(),
                requestDto.getInterest(),
                requestDto.getValueSystem(),
                null,
                requestDto.getShortIntro()
        );
        profileRepository.save(profile);
    }

    @Transactional
    public void uploadProfileImage(MultipartFile file, User user) {
        Long userId = user.getId();
        Profile profile = findByUserId(userId);
        imageService.validateFile(file);
        String fileUrl = imageService.getFileUrl(file);
        profile.addPofileImage(fileUrl);
        profileRepository.save(profile);
    }

    public ProfileResponseDto getProfile(User user) {
        Profile profile = findByUserId(user.getId());
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

    private Profile findByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
