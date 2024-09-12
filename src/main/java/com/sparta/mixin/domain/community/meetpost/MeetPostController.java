package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.community.meetpost.dto.MeetPostRequestDto;
import com.sparta.mixin.domain.community.meetpost.dto.MeetPostResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/meet")
public class MeetPostController {
    private final MeetPostService meetPostService;

    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse<MeetPostResponseDto>> createMeetPost(@PathVariable(name = "meetId") Long meetId,@RequestBody MeetPostRequestDto meetPostRequestDto){
        MeetPostResponseDto responseDto = meetPostService.createMeetPost(meetId,
            meetPostRequestDto);
        CommonResponse response = new CommonResponse("밋커뮤니티에 글 작성 성공",201,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<MeetPostResponseDto>> getMeetPost(@PathVariable(name = "postId")Long postId){
        MeetPostResponseDto responseDto = meetPostService.getMeetPost(postId);
        CommonResponse response = new CommonResponse<>("밋커뮤니티 단건 글 조회 성공",200,responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponse<MeetPostResponseDto>> editMeetPost(@PathVariable(name = "postId")Long postId,@RequestBody MeetPostRequestDto meetPostRequestDto){
        MeetPostResponseDto responseDto = meetPostService.editMeetPost(postId,meetPostRequestDto);
        CommonResponse response = new CommonResponse<>("밋커뮤니티 글 수정 성공",200,responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deleteMeetPost(@PathVariable(name = "postId")Long postId){
        meetPostService.deleteMeetPost(postId);
        CommonResponse response = new CommonResponse("밋커뮤니티 글 삭제 성공",204,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
