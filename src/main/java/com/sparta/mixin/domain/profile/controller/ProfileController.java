package com.sparta.mixin.domain.profile.controller;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.profile.dto.ProfileRequestDto;
import com.sparta.mixin.domain.profile.dto.ProfileResponseDto;
import com.sparta.mixin.domain.profile.service.ProfileService;
import com.sparta.mixin.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<CommonResponse> createProfile(
            @RequestBody @Valid ProfileRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        profileService.createProfile(requestDto, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("프로필 생성이 완료되었습니다.", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/image")
    public ResponseEntity<CommonResponse> uploadProfileImage(
            @RequestPart(value = "file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
            profileService.uploadProfileImage(file, userDetails.getUser());
            CommonResponse response = new CommonResponse<>("프로필 이미지 생성이 완료되었습니다.", 201, "");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<ProfileResponseDto>> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto responseDto = profileService.getProfile(userDetails.getUser());
        CommonResponse response = new CommonResponse<>("프로필 조회가 완료되었습니다.", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/image")
    public ResponseEntity<CommonResponse<Void>> updateProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        profileService.updateProfileImage(file, userDetails.getUser());
        CommonResponse<Void> response = new CommonResponse<>("프로필 이미지 수정이 완료되었습니다.", 200, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<CommonResponse> deleteProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        profileService.deleteProfileImage(userDetails.getUser());
        CommonResponse<Void> response = new CommonResponse<>("프로필 이미지 삭제가 완료되었습니다.", 200, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
