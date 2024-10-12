package com.sparta.mixin.domain.meetapplication.controller;

import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.service.MeetApplicationService;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet/application")
@RequiredArgsConstructor
public class MeetApplicationController {
    private final MeetApplicationService meetApplicationService;

    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse> createMeetApplication(@PathVariable(name = "meetId") Long meeetId, @RequestBody MeetApplicationRequestDto requestDto) {
        meetApplicationService.createMeetApplication(requestDto);
        CommonResponse response = new CommonResponse<>("모임 가입 신청 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{meetApplicationId}")
    public ResponseEntity<CommonResponse> deleteMeetApplication(@PathVariable(name = "meetApplicationId") Long meetApplicationId) {
        meetApplicationService.deleteMeetApplication(meetApplicationId);
        CommonResponse response = new CommonResponse<>("모임 가입 신청 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
