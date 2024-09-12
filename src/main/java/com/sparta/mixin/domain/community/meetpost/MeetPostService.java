package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.community.meetpost.dto.MeetPostRequestDto;
import com.sparta.mixin.domain.community.meetpost.dto.MeetPostResponseDto;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetPostService {
    private final MeetPostRepository meetPostRepository;
    private final MeetService meetService;

    public MeetPostResponseDto createMeetPost(Long meetId, MeetPostRequestDto meetPostRequestDto) {
        Meet meet = meetService.findById(meetId);

        MeetPost meetPost = MeetPost.builder()
            .meetPostRequestDto(meetPostRequestDto)
            .meet(meet)
            .build();

        meetPostRepository.save(meetPost);

    }

    public MeetPostResponseDto getMeetPost(Long postId) {

        MeetPost meetPost = findById(postId);

        MeetPostResponseDto meetPostResponseDto = new MeetPostResponseDto(meetPost);

        return meetPostResponseDto;
    }

    public MeetPostResponseDto editMeetPost(Long postId) {
    }

    public MeetPost findById(Long postId){
        return meetPostRepository.findById(postId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
