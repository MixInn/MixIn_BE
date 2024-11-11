package com.sparta.mixin.domain.post.meetnotice;

import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostRepository;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class MeetNoticeService extends PostService<MeetNotice> {

    private final MeetAuthorizationService meetAuthorizationService;

    public MeetNoticeService(PostRepository<MeetNotice> postRepository,
        ImageRepository imageRepository,
        UserService userService, MeetAuthorizationService meetAuthorizationService,
        MeetService meetService) {
        super(postRepository, imageRepository, userService, meetService);
        this.meetAuthorizationService = meetAuthorizationService;
    }

    @Override
    protected void checkMeetAuthorization(Meet meet, User loginUser) {
        if (meetAuthorizationService.findByMeetAndUser(meet, loginUser) == null) {
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }
    }
}
