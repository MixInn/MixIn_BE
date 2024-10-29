package com.sparta.mixin.domain.meetactivity.controller;

import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.domain.meetactivity.service.MeetActivityService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetActivityController {
    private final MeetActivityService meetActivityService;

    @PostMapping("/{meetId}/activity")
    public ResponseEntity<CommonResponse> createMeetActivity(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetActivityRequestDto requestDto) {
        meetActivityService.createMeetActivity(meetId, requestDto, new User());
        CommonResponse response = new CommonResponse<>("모임 활동 생성 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse> updateMeetActivity(@PathVariable(name = "meetActivityId") Long meetActivityId, @RequestBody MeetActivityRequestDto requestDto) {
        meetActivityService.updateMeetActivity(meetActivityId, requestDto, new User());
        CommonResponse response = new CommonResponse<>("모임 활동 수정 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/activity/{meetActivityId}")
    public ResponseEntity<CommonResponse> deleteMeetActivity(@PathVariable(name = "meetActivityId") Long meetActivityId) {
        meetActivityService.deleteMeetActivity(meetActivityId, new User());
        CommonResponse response = new CommonResponse<>("모임 활동 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
