package com.sparta.mixin.domain.meet.controller;

import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetController {
    private final MeetService meetService;

    @PostMapping
    public ResponseEntity<CommonResponse> createMeet(@RequestBody MeetRequestDto requestDto) {
        meetService.createMeet(requestDto);
        CommonResponse response = new CommonResponse<>("모임 생성 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<CommonResponse> updateMeet(@PathVariable(name = "meetId") Long meetId, @RequestBody MeetRequestDto requestDto) {
        meetService.updateMeet(meetId, requestDto);
        CommonResponse response = new CommonResponse<>("모임 수정 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/{meetId}")
    public ResponseEntity<CommonResponse> deleteMeet(@PathVariable(name = "meetId") Long meetId) {
        meetService.deleteMeet(meetId);
        CommonResponse response = new CommonResponse<>("모임 삭제 성공", 201, "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
