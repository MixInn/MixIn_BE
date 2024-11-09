package com.sparta.mixin.domain.post.like;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.post.like.LikeRepository;
import com.sparta.mixin.domain.post.like.entity.PostLike;
import com.sparta.mixin.domain.post.meetpost.MeetPostService;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.publicpost.PublicPostService;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MeetPostService meetPostService;
    private final PublicPostService publicPostService;
    private final AuthService authService;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public void postPublicLike(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        PostLike postLike = likeRepository.findByPublicPostAndUser(publicPost, loginUser);
        if (postLike != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        PostLike newPostLike = new PostLike(publicPost, loginUser);
        likeRepository.save(newPostLike);
    }

    public void deletePublicLike(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        PostLike publicLike = likeRepository.findByPublicPostAndUser(publicPost, loginUser);
        if (publicLike == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        likeRepository.delete(publicLike);
    }

    public void postMeetLike(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        Meet meet = meetService.findById(meetPost.getMeet().getId());
        if(meetAuthorizationService.findByMeetAndUser(meet,loginUser)==null){
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }

        PostLike postLike = likeRepository.findByMeetPostAndUser(meetPost, loginUser);
        if (postLike != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        PostLike newPostLike = new PostLike(meetPost, loginUser);
        likeRepository.save(newPostLike);
    }

    public void deleteMeetLike(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        PostLike meetLike = likeRepository.findByMeetPostAndUser(meetPost, loginUser);
        if (meetLike == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        likeRepository.delete(meetLike);
    }

    public PostLike findById(Long likeId) {
        return likeRepository.findById(likeId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
