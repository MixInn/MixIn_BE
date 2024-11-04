package com.sparta.mixin.domain.meetannouncement.controller;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet/announcement")
@RequiredArgsConstructor
public class MeetAnnouncementController {
    private final MeetAnnouncementService meetAnnouncementService;

    @GetMapping("/{meetId}")
    public ResponseEntity<CommonResponse> readMeetAnnouncement(@PathVariable(name = "meetId") Long meetId) {
        MeetAnnouncementResponseDto responseDto = meetAnnouncementService.readMeetAnnouncement(meetId);
        CommonResponse response = new CommonResponse<>("모임 공고 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse> createMeetAnnouncement(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetAnnouncementRequestDto requestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetAnnouncementService.createMeetAnnouncement(meetId, requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 공고 생성 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<CommonResponse> updateMeetAnnouncement(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetAnnouncementRequestDto requestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        meetAnnouncementService.updateMeetAnnouncement(meetId, requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("모임 공고 수정 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
