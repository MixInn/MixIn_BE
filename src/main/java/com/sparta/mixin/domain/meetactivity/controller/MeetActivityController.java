package com.sparta.mixin.domain.meetactivity.controller;

import com.sparta.mixin.domain.meetactivity.dto.MeetActivityListResponseDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityResponseDto;
import com.sparta.mixin.domain.meetactivity.service.MeetActivityService;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetActivityController {
    private final MeetActivityService meetActivityService;

    /**
     * 특정 모임의 활동 리스트 조회
     */
    @GetMapping("/{meetId}/activities")
    public ResponseEntity<CommonResponse<List<MeetActivityListResponseDto>>> getActivitiesForMeet(
            @PathVariable Long meetId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<MeetActivityListResponseDto> responseDtoList = meetActivityService.getActivitiesForMeet(meetId, userDetails.getUser());
        return ResponseEntity.ok(new CommonResponse<>("모임 활동 리스트 조회 성공", 200, responseDtoList));
    }


    /**
     * 활동 조회 (참가자 포함)
     */
    @GetMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<MeetActivityResponseDto>> getMeetActivityWithParticipants(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MeetActivityResponseDto responseDto = meetActivityService.getMeetActivityWithParticipants(meetActivityId, userDetails.getUser());
        return ResponseEntity.ok(new CommonResponse<>("모임 활동 조회 성공", 200, responseDto));
    }

    /**
     * 활동 생성
     */
    @PostMapping("/{meetId}/activity")
    public ResponseEntity<CommonResponse<Void>> createMeetActivity(
            @PathVariable Long meetId,
            @RequestBody MeetActivityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.createMeetActivity(meetId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("모임 활동 생성 성공", 201, null));
    }

    /**
     * 활동 수정
     */
    @PutMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<Void>> updateMeetActivity(
            @PathVariable Long meetActivityId,
            @RequestBody MeetActivityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.updateMeetActivity(meetActivityId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("모임 활동 수정 성공", 201, null));
    }

    /**
     * 활동 삭제
     */
    @DeleteMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<Void>> deleteMeetActivity(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.deleteMeetActivity(meetActivityId, userDetails.getUser());
        return ResponseEntity.ok(new CommonResponse<>("모임 활동 삭제 성공", 200, null));
    }

    /**
     * 활동 참여
     */
    @PostMapping("/activity/{meetActivityId}/join")
    public ResponseEntity<CommonResponse<Void>> addParticipant(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.joinActivity(meetActivityId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("활동 참여 성공", 201, null));
    }

    /**
     * 활동 참여 취소
     */
    @DeleteMapping("/activity/{meetActivityId}/leave")
    public ResponseEntity<CommonResponse<Void>> removeParticipant(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.leaveActivity(meetActivityId, userDetails.getUser());
        return ResponseEntity.ok(new CommonResponse<>("활동 참여 취소 성공", 200, null));
    }
}
