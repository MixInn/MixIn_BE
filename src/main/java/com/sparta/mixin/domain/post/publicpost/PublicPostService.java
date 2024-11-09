package com.sparta.mixin.domain.post.publicpost;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostRepository;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class PublicPostService extends PostService<PublicPost> {

    public PublicPostService(PostRepository<PublicPost> postRepository,
        ImageRepository imageRepository,
        AuthService authService, MeetService meetService) {
        super(postRepository, imageRepository, authService,meetService);
    }

    @Override
    protected void checkMeetAuthorization(Meet meet, User loginUser) {

    }
}
