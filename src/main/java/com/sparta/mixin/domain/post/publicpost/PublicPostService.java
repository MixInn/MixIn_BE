package com.sparta.mixin.domain.post.publicpost;

import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostRepository;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.post.meetnotice.MeetNoticeRepository;
import com.sparta.mixin.domain.post.meetpost.MeetPostRepository;
import com.sparta.mixin.domain.post.noticeread.NoticeReadRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class PublicPostService extends PostService<PublicPost> {

    public PublicPostService(PostRepository<PublicPost> postRepository,
        MeetPostRepository meetPostRepository,
        MeetNoticeRepository meetNoticeRepository,
        ImageRepository imageRepository,
        UserService userService, MeetService meetService,
        NoticeReadRepository noticeReadRepository) {
        super(postRepository, meetPostRepository,meetNoticeRepository,imageRepository, userService, meetService,noticeReadRepository);
    }

    @Override
    protected void checkMeetAuthorization(Meet meet, User loginUser) {

    }
}
