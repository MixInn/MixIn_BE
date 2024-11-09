package com.sparta.mixin.domain.post;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;

public class PostFactory {

    public static Post createPost(PostRequestDto postRequestDto, User user, String postType, Meet meet) {
        switch (postType) {
            case "PUBLICPOST":
                return new PublicPost(postRequestDto, user);
            case "MEETPOST":
                return new MeetPost(postRequestDto, user, meet);
            case "MEETNOTICE":
                return new MeetNotice(postRequestDto, user, meet);
            default:
                throw new CustomException(ErrorCode.INVALID_POST_TYPE);
        }
    }
}
