package com.sparta.mixin.domain.post.meetpost;

import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostRepository;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.meetnotice.MeetNoticeRepository;
import com.sparta.mixin.domain.post.noticeread.NoticeReadRepository;
import com.sparta.mixin.domain.post.publicpost.PublicPostRepository;
import com.sparta.mixin.domain.post.vote.repository.PostVoteRepository;
import com.sparta.mixin.domain.post.vote.repository.VoteOptionRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class MeetPostService extends PostService<MeetPost> {

    private final MeetAuthorizationService meetAuthorizationService;

    public MeetPostService(PostRepository<MeetPost> postRepository,
        MeetPostRepository meetPostRepository,
        MeetNoticeRepository meetNoticeRepository,
        PublicPostRepository publicPostRepository,
        ImageRepository imageRepository,
        UserService userService, MeetAuthorizationService meetAuthorizationService,
        MeetService meetService,
        NoticeReadRepository noticeReadRepository,
        PostVoteRepository postVoteRepository,
        VoteOptionRepository voteOptionRepository) {
        super(postRepository, meetPostRepository, meetNoticeRepository,publicPostRepository,imageRepository, userService, meetService,noticeReadRepository,postVoteRepository,voteOptionRepository);
        this.meetAuthorizationService = meetAuthorizationService;
    }

    // 후크 메서드를 오버라이드하여 MeetPost에만 필요한 로직 추가
    @Override
    protected void checkMeetAuthorization(Meet meet, User loginUser) {
        if (meetAuthorizationService.findByMeetAndUser(meet, loginUser) == null) {
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }
    }
}
