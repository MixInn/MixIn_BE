package com.sparta.mixin.domain.community.like;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.community.like.entity.CommunityLike;
import com.sparta.mixin.domain.community.meetpost.MeetPostService;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.PublicPostService;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
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

    public void postPublicLike(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityLike communityLike = likeRepository.findByPublicPostAndUser(publicPost, loginUser);
        if (communityLike != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityLike newCommunityLike = new CommunityLike(publicPost, loginUser);
        likeRepository.save(newCommunityLike);
    }

    public void deletePublicLike(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityLike publicLike = likeRepository.findByPublicPostAndUser(publicPost, loginUser);
        if (publicLike == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        likeRepository.delete(publicLike);
    }

    public void postMeetLike(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityLike communityLike = likeRepository.findByMeetPostAndUser(meetPost, loginUser);
        if (communityLike != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityLike newCommunityLike = new CommunityLike(meetPost, loginUser);
        likeRepository.save(newCommunityLike);
    }

    public void deleteMeetLike(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityLike meetLike = likeRepository.findByMeetPostAndUser(meetPost, loginUser);
        if (meetLike == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        likeRepository.delete(meetLike);
    }

    public CommunityLike findById(Long likeId) {
        return likeRepository.findById(likeId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
