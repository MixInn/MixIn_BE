package com.sparta.mixin.domain.community.like;

import com.sparta.mixin.domain.community.like.entity.CommunityLike;
import com.sparta.mixin.domain.community.meetpost.MeetPostService;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.PublicPostService;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
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

    public void postPublicLike(Long postId) {
        PublicPost publicPost = publicPostService.findById(postId);
        CommunityLike communityLike = likeRepository.findByPublicPost(publicPost);
        if (communityLike != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityLike newCommunityLike = new CommunityLike(publicPost);
        likeRepository.save(newCommunityLike);
    }

    public void deletePublicLike(Long likeId) {
        CommunityLike communityLike = findById(likeId);
        likeRepository.delete(communityLike);
    }

    public void postMeetLike(Long postId) {
        MeetPost meetPost = meetPostService.findById(postId);
        CommunityLike communityLike = likeRepository.findByMeetPost(meetPost);
        if (communityLike != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityLike newCommunityLike = new CommunityLike(meetPost);
        likeRepository.save(newCommunityLike);
    }

    public void deleteMeetLike(Long likeId) {
        CommunityLike communityLike = findById(likeId);
        likeRepository.delete(communityLike);
    }

    public CommunityLike findById(Long likeId) {
        return likeRepository.findById(likeId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
