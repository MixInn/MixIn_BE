package com.sparta.mixin.domain.meetapplication.controller;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationResponseDto;
import com.sparta.mixin.domain.meetapplication.service.MeetApplicationService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetApplicationController {
    private final MeetApplicationService meetApplicationService;

    @PostMapping("/meet/application/{meetAnnouncementId}")
    public ResponseEntity<CommonResponse> createMeetApplication(@PathVariable(name = "meetAnnouncementId") Long meetAnnouncementId,
                                                                @RequestBody MeetApplicationRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.createMeetApplication(meetAnnouncementId,requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 가입 신청 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/meet/application/{meetApplicationId}")
    public ResponseEntity<CommonResponse> updateMeetApplication(@PathVariable Long meetApplicationId,
                                                                @RequestBody MeetApplicationRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.updateMeetApplication(meetApplicationId, requestDto, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 가입 신청 수정 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meet/application/{meetApplicationId}")
    public ResponseEntity<CommonResponse> deleteMeetApplication(@PathVariable(name = "meetApplicationId") Long meetApplicationId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.deleteMeetApplication(meetApplicationId,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 가입 신청 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/meet/application/{meetApplicationId}/accept")
    public ResponseEntity<CommonResponse> acceptMeetApplication(@PathVariable Long meetApplicationId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.acceptMeetApplication(meetApplicationId);
        CommonResponse response = new CommonResponse<>("가입 신청서 승락 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/meet/application/{meetApplicationId}/reject")
    public ResponseEntity<CommonResponse> rejectMeetApplication(@PathVariable Long meetApplicationId,
                                                                @RequestParam String reason,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.rejectMeetApplication(meetApplicationId, reason);
        CommonResponse response = new CommonResponse<>("가입 신청서 거절 완료", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/meet/application/my")
    public ResponseEntity<CommonResponse> getMyApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MeetApplicationResponseDto> applications = meetApplicationService.getMyApplications(userDetails.getUser());
        CommonResponse response = new CommonResponse<>("신청한 모임 가입 신청서 조회 성공", 200, applications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/meet/{meetId}/applications")
    public ResponseEntity<CommonResponse> getApplicationsForMeet(@PathVariable Long meetId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MeetApplicationResponseDto> applications = meetApplicationService.getApplicationsForMeet(meetId, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임에 대한 가입 신청서 조회 성공", 200, applications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
