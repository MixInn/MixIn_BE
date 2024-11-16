package com.sparta.mixin.domain.meetapplication.controller;

import com.sparta.mixin.global.security.UserDetailsImpl;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationResponseDto;
import com.sparta.mixin.domain.meetapplication.service.MeetApplicationService;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetApplicationController {
    private final MeetApplicationService meetApplicationService;
    private static final Logger log = LoggerFactory.getLogger(MeetApplicationController.class);

    @PostMapping("/meet/application/{meetAnnouncementId}")
    public ResponseEntity<CommonResponse> createMeetApplication(@PathVariable(name = "meetAnnouncementId") Long meetAnnouncementId,
                                                                @RequestBody MeetApplicationRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 가입 신청 시작 - 사용자 ID: {}, 모임 공고 ID: {}", userDetails.getUser().getId(), meetAnnouncementId);

        meetApplicationService.createMeetApplication(meetAnnouncementId,requestDto,userDetails.getUser());

        log.info("모임 가입 신청 성공 - 사용자 ID: {}, 모임 공고 ID: {}", userDetails.getUser().getId(), meetAnnouncementId);
        CommonResponse response = new CommonResponse<>("모임 가입 신청 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/meet/application/{meetApplicationId}")
    public ResponseEntity<CommonResponse> updateMeetApplication(@PathVariable Long meetApplicationId,
                                                                @RequestBody MeetApplicationRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 가입 신청 수정 시작 - 사용자 ID: {}, 신청서 ID: {}", userDetails.getUser().getId(), meetApplicationId);

        meetApplicationService.updateMeetApplication(meetApplicationId, requestDto, userDetails.getUser());

        log.info("모임 가입 신청 수정 완료 - 사용자 ID: {}, 신청서 ID: {}", userDetails.getUser().getId(), meetApplicationId);
        CommonResponse response = new CommonResponse<>("모임 가입 신청 수정 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meet/application/{meetApplicationId}")
    public ResponseEntity<CommonResponse> deleteMeetApplication(@PathVariable(name = "meetApplicationId") Long meetApplicationId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 가입 신청 삭제 시작 - 사용자 ID: {}, 신청서 ID: {}", userDetails.getUser().getId(), meetApplicationId);

        meetApplicationService.deleteMeetApplication(meetApplicationId,userDetails.getUser());

        log.info("모임 가입 신청 삭제 완료 - 사용자 ID: {}, 신청서 ID: {}", userDetails.getUser().getId(), meetApplicationId);
        CommonResponse response = new CommonResponse<>("모임 가입 신청 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/meet/application/{meetApplicationId}/accept")
    public ResponseEntity<CommonResponse> acceptMeetApplication(@PathVariable Long meetApplicationId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 가입 신청 수락 시작 - 신청서 ID: {}", meetApplicationId);

        meetApplicationService.acceptMeetApplication(meetApplicationId);

        log.info("모임 가입 신청 수락 완료 - 신청서 ID: {}", meetApplicationId);
        CommonResponse response = new CommonResponse<>("가입 신청서 승락 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/meet/application/{meetApplicationId}/reject")
    public ResponseEntity<CommonResponse> rejectMeetApplication(@PathVariable Long meetApplicationId,
                                                                @RequestParam String reason,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 가입 신청 거절 시작 - 신청서 ID: {}, 사용자 ID: {}", meetApplicationId, userDetails.getUser().getId());

        meetApplicationService.rejectMeetApplication(meetApplicationId, reason);

        log.info("모임 가입 신청 거절 완료 - 신청서 ID: {}, 사용자 ID: {}", meetApplicationId, userDetails.getUser().getId());
        CommonResponse response = new CommonResponse<>("가입 신청서 거절 완료", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/meet/application/my")
    public ResponseEntity<CommonResponse> getMyApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("사용자 ID: {} 의 모든 모임 가입 신청서 조회 시작", userDetails.getUser().getId());

        List<MeetApplicationResponseDto> applications = meetApplicationService.getMyApplications(userDetails.getUser());

        log.info("사용자 ID: {} 의 모든 모임 가입 신청서 조회 완료", userDetails.getUser().getId());
        CommonResponse response = new CommonResponse<>("신청한 모임 가입 신청서 조회 성공", 200, applications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/meet/{meetId}/applications")
    public ResponseEntity<CommonResponse> getApplicationsForMeet(@PathVariable Long meetId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 ID: {} 의 모든 가입 신청서 조회 시작 - 사용자 ID: {}", meetId, userDetails.getUser().getId());

        List<MeetApplicationResponseDto> applications = meetApplicationService.getApplicationsForMeet(meetId, userDetails.getUser());

        log.info("모임 ID: {} 의 모든 가입 신청서 조회 완료 - 사용자 ID: {}", meetId, userDetails.getUser().getId());
        CommonResponse response = new CommonResponse<>("모임에 대한 가입 신청서 조회 성공", 200, applications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
