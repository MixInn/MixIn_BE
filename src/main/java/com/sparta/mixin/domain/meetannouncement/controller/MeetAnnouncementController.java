package com.sparta.mixin.domain.meetannouncement.controller;

import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncmentService;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet/announcement")
@RequiredArgsConstructor
public class MeetAnnouncementController {
    private final MeetAnnouncmentService meetAnnouncmentService;

    @GetMapping("/{meetId}")
    public ResponseEntity<CommonResponse> readMeetAnnouncment(@PathVariable(name = "meetId") Long meetId) {
        MeetAnnouncementResponseDto responseDto =meetAnnouncmentService.readMeetAnnouncment(meetId);
        CommonResponse response = new CommonResponse<>("모임 공고 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse> createMeetAnnouncment(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetAnnouncementRequestDto requestDto) {
        meetAnnouncmentService.createMeetAnnouncment(meetId, requestDto);
        CommonResponse response = new CommonResponse<>("모임 공고 생성 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<CommonResponse> updateMeetAnnouncment(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetAnnouncementRequestDto requestDto) {
        meetAnnouncmentService.updateMeetAnnouncment(meetId, requestDto);
        CommonResponse response = new CommonResponse<>("모임 공고 수정 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
