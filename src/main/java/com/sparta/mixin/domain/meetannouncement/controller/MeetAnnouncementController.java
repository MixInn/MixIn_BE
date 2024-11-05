package com.sparta.mixin.domain.meetannouncement.controller;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementListRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet/announcement")
@RequiredArgsConstructor
public class MeetAnnouncementController {
    private final MeetAnnouncementService meetAnnouncementService;

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> getAnnouncementList(
            @RequestParam(required = false) String meetType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String meetName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        MeetAnnouncementListRequestDto requestDto = new MeetAnnouncementListRequestDto(meetType, category, tags, meetName, page, size);
        Page<MeetAnnouncementResponseDto> responseDtoPage = meetAnnouncementService.getAnnouncementList(requestDto);
        CommonResponse response = new CommonResponse<>("모임 공고 리스트 조회 성공", 200, responseDtoPage);
        return ResponseEntity.ok(response);
    }

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
