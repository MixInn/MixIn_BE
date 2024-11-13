package com.sparta.mixin.domain.meet.controller;

import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetType;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetController {
    private final MeetService meetService;
    private final ImageService imageService;
    private final MeetAnnouncementService meetAnnouncementService;

    @PostMapping
    public ResponseEntity<CommonResponse> createMeet(@RequestPart("requestDto") MeetRequestDto requestDto,
                                                     @RequestPart(value = "file", required = false) MultipartFile file,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (file != null && !file.isEmpty()) {
            imageService.validateFile(file); // 파일 유효성 검사
            String imageUrl = imageService.getFileUrl(file); // 파일 저장 후 URL 생성
            requestDto.setImage(imageUrl); // 새로운 이미지 URL 설정
        }

        Meet savedMeet = meetService.createMeet(requestDto,userDetails.getUser());
        MeetType meetType = MeetType.fromString(requestDto.getType());

        if (meetType == MeetType.LIGHTNING) {
            MeetAnnouncementRequestDto announcementRequestDto = MeetAnnouncementRequestDto.builder()
                    .recruitmentPeriod(requestDto.getRecruitmentPeriod())
                    .gender(requestDto.getGender())
                    .numberOfPeople(requestDto.getNumberOfPeople())
                    .meetTime(requestDto.getMeetTime())
                    .tag(requestDto.getTag())
                    .build();
            meetAnnouncementService.createLightingAnnouncement(savedMeet.getId(),announcementRequestDto,userDetails.getUser());
        }

        CommonResponse response = new CommonResponse<>("모임 생성 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<CommonResponse> updateMeet(@PathVariable(name = "meetId") Long meetId,
                                                     @RequestPart("requestDto") MeetRequestDto requestDto,
                                                     @RequestPart(value = "file", required = false) MultipartFile file,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // file이 비어있지 않으면 새로운 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            imageService.validateFile(file); // 파일 유효성 검사
            String imageUrl = imageService.getFileUrl(file); // 파일 저장 후 URL 생성
            requestDto.setImage(imageUrl); // 새로운 이미지 URL 설정
        }
        meetService.updateMeet(meetId, requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 수정 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/{meetId}")
    public ResponseEntity<CommonResponse> deleteMeet(@PathVariable(name = "meetId") Long meetId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetService.deleteMeet(meetId,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
