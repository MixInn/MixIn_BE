package com.sparta.mixin.domain.meetapplication.controller;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.service.MeetApplicationService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet/application")
@RequiredArgsConstructor
public class MeetApplicationController {
    private final MeetApplicationService meetApplicationService;

    @PostMapping("/{meetAnnouncementId}")
    public ResponseEntity<CommonResponse> createMeetApplication(@PathVariable(name = "meetAnnouncementId") Long meetAnnouncementId,
                                                                @RequestBody MeetApplicationRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.createMeetApplication(meetAnnouncementId,requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 가입 신청 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update an existing application
    @PutMapping("/{meetApplicationId}")
    public ResponseEntity<CommonResponse> updateMeetApplication(@PathVariable Long meetApplicationId,
                                                                @RequestBody MeetApplicationRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.updateMeetApplication(meetApplicationId, requestDto, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("Application updated successfully", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{meetApplicationId}")
    public ResponseEntity<CommonResponse> deleteMeetApplication(@PathVariable(name = "meetApplicationId") Long meetApplicationId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.deleteMeetApplication(meetApplicationId,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 가입 신청 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Accept an application
    @PostMapping("/{meetApplicationId}/accept")
    public ResponseEntity<CommonResponse> acceptMeetApplication(@PathVariable Long meetApplicationId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.acceptMeetApplication(meetApplicationId);
        CommonResponse response = new CommonResponse<>("Application accepted successfully", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Reject an application
    @PostMapping("/{meetApplicationId}/reject")
    public ResponseEntity<CommonResponse> rejectMeetApplication(@PathVariable Long meetApplicationId,
                                                                @RequestParam String reason,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetApplicationService.rejectMeetApplication(meetApplicationId, reason);
        CommonResponse response = new CommonResponse<>("Application rejected successfully", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
