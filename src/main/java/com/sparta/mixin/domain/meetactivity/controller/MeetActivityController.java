package com.sparta.mixin.domain.meetactivity.controller;

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

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetActivityController {
    private final MeetActivityService meetActivityService;

    // 활동 조회 (참가자 포함)
    @GetMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<MeetActivityResponseDto>> getMeetActivityWithParticipants(
            @PathVariable(name = "meetActivityId") Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MeetActivityResponseDto responseDto = meetActivityService.getMeetActivityWithParticipants(meetActivityId, userDetails.getUser());
        CommonResponse<MeetActivityResponseDto> response = new CommonResponse<>("모임 활동 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 활동 생성
    @PostMapping("/{meetId}/activity")
    public ResponseEntity<CommonResponse> createMeetActivity(
            @PathVariable(name = "meetId") Long meetId,
            @RequestBody MeetActivityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.createMeetActivity(meetId, requestDto, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 활동 생성 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 활동 수정
    @PutMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse> updateMeetActivity(
            @PathVariable(name = "meetActivityId") Long meetActivityId,
            @RequestBody MeetActivityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.updateMeetActivity(meetActivityId, requestDto, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 활동 수정 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // 활동 삭제
    @DeleteMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse> deleteMeetActivity(
            @PathVariable(name = "meetActivityId") Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.deleteMeetActivity(meetActivityId, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 활동 삭제 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 활동 참여
    @PostMapping("/activity/{meetActivityId}/join")
    public ResponseEntity<CommonResponse> addParticipant(
            @PathVariable(name = "meetActivityId") Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.joinActivity(meetActivityId, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("활동 참여 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 활동 참여 취소
    @DeleteMapping("/activity/{meetActivityId}/leave")
    public ResponseEntity<CommonResponse> removeParticipant(
            @PathVariable(name = "meetActivityId") Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        meetActivityService.leaveActivity(meetActivityId, userDetails.getUser());
        CommonResponse response = new CommonResponse<>("활동 참여 취소 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
