package com.sparta.mixin.domain.post.like;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.like.entity.PostLike;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public void postLike(Long postId, User user) {
        Post post = postService.findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (post instanceof MeetPost) {
            Meet meet = meetService.findById(((MeetPost) post).getMeet().getId());
            if (meetAuthorizationService.findByMeetAndUser(meet, loginUser) == null) {
                throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
            }
        }

        if (post instanceof MeetNotice) {
            Meet meet = meetService.findById(((MeetNotice) post).getMeet().getId());
            if (meetAuthorizationService.findByMeetAndUser(meet, loginUser) == null) {
                throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
            }
        }

        PostLike postLike = likeRepository.findByPostAndUser(post, loginUser);
        if (postLike != null) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_Like);
        }
        PostLike newPostLike = new PostLike(post, loginUser);
        likeRepository.save(newPostLike);
        post.increaseLikeCount();
        postService.save(post);
    }

    public void deleteLike(Long postId, User user) {
        Post post = postService.findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        PostLike postLike = likeRepository.findByPostAndUser(post, loginUser);
        if (postLike == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        likeRepository.delete(postLike);
        post.decreaseLikeCount();
        postService.save(post);
    }

    public PostLike findById(Long likeId) {
        return likeRepository.findById(likeId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
