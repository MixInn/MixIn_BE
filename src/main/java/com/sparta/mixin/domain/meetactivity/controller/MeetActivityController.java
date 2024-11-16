package com.sparta.mixin.domain.meetactivity.controller;

import com.sparta.mixin.domain.meetactivity.dto.MeetActivityListResponseDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityResponseDto;
import com.sparta.mixin.domain.meetactivity.service.MeetActivityService;
import com.sparta.mixin.domain.meetannouncement.controller.MeetAnnouncementController;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(MeetActivityController.class);

    @GetMapping("/{meetId}/activities")
    public ResponseEntity<CommonResponse<List<MeetActivityListResponseDto>>> getActivitiesForMeet(
            @PathVariable Long meetId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 리스트 조회 요청 - 모임 ID: {}, 사용자: {}", meetId, userDetails.getUsername());
        List<MeetActivityListResponseDto> responseDtoList = meetActivityService.getActivitiesForMeet(meetId, userDetails.getUser());
        log.info("모임 활동 리스트 조회 성공 - 모임 ID: {}", meetId);
        return ResponseEntity.ok(new CommonResponse<>("모임 활동 리스트 조회 성공", 200, responseDtoList));
    }


    @GetMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<MeetActivityResponseDto>> getMeetActivityWithParticipants(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 상세 조회 요청 - 활동 ID: {}, 사용자: {}", meetActivityId, userDetails.getUsername());
        MeetActivityResponseDto responseDto = meetActivityService.getMeetActivityWithParticipants(meetActivityId, userDetails.getUser());
        log.info("모임 활동 상세 조회 성공 - 활동 ID: {}", meetActivityId);
        return ResponseEntity.ok(new CommonResponse<>("모임 활동 조회 성공", 200, responseDto));
    }


    @PostMapping("/{meetId}/activity")
    public ResponseEntity<CommonResponse<Void>> createMeetActivity(
            @PathVariable Long meetId,
            @RequestBody MeetActivityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 생성 요청 - 모임 ID: {}, 사용자: {}, 요청 데이터: {}", meetId, userDetails.getUsername(), requestDto);
        meetActivityService.createMeetActivity(meetId, requestDto, userDetails.getUser());
        log.info("모임 활동 생성 성공 - 모임 ID: {}, 제목: {}", meetId, requestDto.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("모임 활동 생성 성공", 201, null));
    }

    @PutMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<Void>> updateMeetActivity(
            @PathVariable Long meetActivityId,
            @RequestBody MeetActivityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 수정 요청 - 활동 ID: {}, 사용자: {}, 요청 데이터: {}", meetActivityId, userDetails.getUsername(), requestDto);
        meetActivityService.updateMeetActivity(meetActivityId, requestDto, userDetails.getUser());
        log.info("모임 활동 수정 성공 - 활동 ID: {}", meetActivityId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("모임 활동 수정 성공", 201, null));
    }


    @DeleteMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse<Void>> deleteMeetActivity(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 삭제 요청 - 활동 ID: {}, 사용자: {}", meetActivityId, userDetails.getUsername());
        meetActivityService.deleteMeetActivity(meetActivityId, userDetails.getUser());
        log.info("모임 활동 삭제 성공 - 활동 ID: {}", meetActivityId);
        return ResponseEntity.ok(new CommonResponse<>("모임 활동 삭제 성공", 200, null));
    }

    @PostMapping("/activity/{meetActivityId}/join")
    public ResponseEntity<CommonResponse<Void>> addParticipant(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 참여 요청 - 활동 ID: {}, 사용자: {}", meetActivityId, userDetails.getUsername());
        meetActivityService.joinActivity(meetActivityId, userDetails.getUser());
        log.info("모임 활동 참여 성공 - 활동 ID: {}, 사용자: {}", meetActivityId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("활동 참여 성공", 201, null));
    }


    @DeleteMapping("/activity/{meetActivityId}/leave")
    public ResponseEntity<CommonResponse<Void>> removeParticipant(
            @PathVariable Long meetActivityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 활동 참여 취소 요청 - 활동 ID: {}, 사용자: {}", meetActivityId, userDetails.getUsername());
        meetActivityService.leaveActivity(meetActivityId, userDetails.getUser());
        log.info("모임 활동 참여 취소 성공 - 활동 ID: {}, 사용자: {}", meetActivityId, userDetails.getUsername());
        return ResponseEntity.ok(new CommonResponse<>("활동 참여 취소 성공", 200, null));
    }
}
