package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.community.meetpost.dto.MeetPostRequestDto;
import com.sparta.mixin.domain.community.meetpost.dto.MeetPostResponseDto;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetPostService {

    private final MeetPostRepository meetPostRepository;
    private final MeetService meetService;
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final MeetAuthorizationService meetAuthorizationService;

    public MeetPostResponseDto createMeetPost(Long meetId, MeetPostRequestDto meetPostRequestDto,
        List<String> fileUrls, User user) {
        Meet meet = meetService.findById(meetId);
        User loginUser = userService.findByUsername(user.getUsername());

        if(meetAuthorizationService.findByMeetAndUser(meet,loginUser)==null){
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }

        MeetPost meetPost = MeetPost.builder()
            .meetPostRequestDto(meetPostRequestDto)
            .meet(meet)
            .user(loginUser)
            .build();

        meetPostRepository.save(meetPost);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl, meetPost);
            imageRepository.save(image);
        }

        return new MeetPostResponseDto(meetPost);
    }

    public MeetPostResponseDto getMeetPost(Long postId, User user) {

        MeetPost meetPost = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        Meet meet = meetService.findById(meetPost.getMeet().getId());
        if(meetAuthorizationService.findByMeetAndUser(meet,loginUser)==null){
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }

        return new MeetPostResponseDto(meetPost);
    }

    public MeetPostResponseDto editMeetPost(Long postId, MeetPostRequestDto meetPostRequestDto,
        List<String> fileUrls, User user) {
        MeetPost meetPost = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (meetPost.getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        meetPost.updatePost(meetPostRequestDto);
        meetPostRepository.save(meetPost);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl, meetPost);
            imageRepository.save(image);
        }

        return new MeetPostResponseDto(meetPost);
    }

    @Transactional
    public void deleteMeetPost(Long postId, User user) {
        MeetPost meetPost = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (meetPost.getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        meetPostRepository.delete(meetPost);
    }

    public Page<MeetPostResponseDto> getAllMeetPost(Long meetId, int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        Meet meet = meetService.findById(meetId);
        User loginUser = userService.findByUsername(user.getUsername());

        if(meetAuthorizationService.findByMeetAndUser(meet,loginUser)==null){
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }

        Page<MeetPost> responsePage = meetPostRepository.findAllByMeet(meet, pageable);

        return responsePage.map(MeetPostResponseDto::new);
    }

    public MeetPost findById(Long postId) {
        return meetPostRepository.findById(postId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
