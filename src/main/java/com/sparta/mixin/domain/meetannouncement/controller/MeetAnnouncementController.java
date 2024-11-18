package com.sparta.mixin.domain.meetannouncement.controller;

import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementListRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementRequestDto;
import com.sparta.mixin.domain.meetannouncement.dto.MeetAnnouncementResponseDto;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(MeetAnnouncementController.class);

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> getAnnouncementList(
            @RequestParam(required = false) String meetType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String meetName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sortType, // 정렬 방식
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("모임 공고 목록 조회 시작 - 사용자 ID: {}, 필터 - 타입: {}, 카테고리: {}, 태그: {}, 이름: {} 정렬타입: {}",
                userDetails.getUser().getId(), meetType, category, tags, meetName, sortType);
        MeetAnnouncementListRequestDto requestDto = new MeetAnnouncementListRequestDto(meetType, category, tags, meetName, sortType, page, size);
        Page<MeetAnnouncementResponseDto> responseDtoPage = meetAnnouncementService.getAnnouncementList(requestDto,userDetails.getUser());

        log.info("모임 공고 목록 조회 완료 - 조회된 공고 수: {}", responseDtoPage.getTotalElements());
        CommonResponse response = new CommonResponse<>("모임 공고 리스트 조회 성공", 200, responseDtoPage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{AnnouncementId}")
    public ResponseEntity<CommonResponse> readMeetAnnouncement(@PathVariable(name = "AnnouncementId") Long AnnouncementId) {
        log.info("모임 공고 조회 시작 - 모임 공고 ID: {}", AnnouncementId);

        MeetAnnouncementResponseDto responseDto = meetAnnouncementService.readMeetAnnouncement(AnnouncementId);

        log.info("모임 공고 조회 완료 - 모임 공고 ID: {}", AnnouncementId);
        CommonResponse response = new CommonResponse<>("모임 공고 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse> createMeetAnnouncement(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetAnnouncementRequestDto requestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 공고 생성 시작 - 사용자 ID: {}, 모임 ID: {}, 요청 데이터: {}", userDetails.getUser().getId(), meetId, requestDto);

        meetAnnouncementService.createMeetAnnouncement(meetId, requestDto,userDetails.getUser());

        log.info("모임 공고 생성 완료 - 모임 ID: {}", meetId);
        CommonResponse response = new CommonResponse<>("모임 공고 생성 성공", 201, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<CommonResponse> updateMeetAnnouncement(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetAnnouncementRequestDto requestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("모임 공고 수정 시작 - 사용자 ID: {}, 모임 ID: {}, 요청 데이터: {}", userDetails.getUser().getId(), meetId, requestDto);

        meetAnnouncementService.updateMeetAnnouncement(meetId, requestDto,userDetails.getUser());

        log.info("모임 공고 수정 완료 - 모임 ID: {}", meetId);
        CommonResponse response = new CommonResponse<>("모임 공고 수정 성공", 200, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
