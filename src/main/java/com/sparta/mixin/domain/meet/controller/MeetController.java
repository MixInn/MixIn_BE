package com.sparta.mixin.domain.meet.controller;

import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(MeetController.class);

    @PostMapping
    public ResponseEntity<CommonResponse> createMeet(@RequestPart("requestDto") MeetRequestDto requestDto,
                                                     @RequestPart(value = "file", required = false) MultipartFile file,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 생성 요청 시작 - 사용자 ID: {}", userDetails.getUser().getId());

        handleFileUpload(requestDto, file, "모임 생성");

        Meet savedMeet = meetService.createMeet(requestDto,userDetails.getUser());
        log.info("모임 생성 완료 - 사용자 ID: {}, 모임 ID: {}", userDetails.getUser().getId(), savedMeet.getId());

        CommonResponse response = new CommonResponse<>("모임 생성 성공", 201, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<CommonResponse> updateMeet(@PathVariable(name = "meetId") Long meetId,
                                                     @RequestPart("requestDto") MeetRequestDto requestDto,
                                                     @RequestPart(value = "file", required = false) MultipartFile file,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 수정 요청 시작 - 사용자 ID: {}, 모임 ID: {}", userDetails.getUser().getId(), meetId);
        // 사진 업로드
        handleFileUpload(requestDto, file, "모임 수정");

        // 모임 수정
        meetService.updateMeet(meetId, requestDto,userDetails.getUser());
        log.info("모임 수정 완료 - 사용자 ID: {}, 모임 ID: {}", userDetails.getUser().getId(), meetId);

        CommonResponse response = new CommonResponse<>("모임 수정 성공", 201, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/{meetId}")
    public ResponseEntity<CommonResponse> deleteMeet(@PathVariable(name = "meetId") Long meetId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 삭제 요청 시작 - 사용자 ID: {}, 모임 ID: {}", userDetails.getUser().getId(), meetId);

        meetService.deleteMeet(meetId,userDetails.getUser());
        log.info("모임 삭제 완료 - 사용자 ID: {}, 모임 ID: {}", userDetails.getUser().getId(), meetId);
        CommonResponse response = new CommonResponse<>("모임 삭제 성공", 201, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * 파일 업로드 처리 메서드
     *
     * @param requestDto 모임 요청 DTO
     * @param file 업로드된 파일
     * @param action 현재 작업 (예: "모임 생성", "모임 수정")
     */
    private void handleFileUpload(MeetRequestDto requestDto, MultipartFile file, String action) {
        if (file != null && !file.isEmpty()) {
                imageService.validateFile(file);
                String imageUrl = imageService.getFileUrl(file);
                requestDto.setImage(imageUrl);
                log.debug("{} 파일 업로드 성공 - 이미지 URL: {}", action, imageUrl);
        }
    }
}
