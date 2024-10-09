package com.sparta.mixin.domain.community.comment;

import com.sparta.mixin.domain.community.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.community.comment.dto.CommentResponseDto;
import com.sparta.mixin.domain.community.comment.entity.CommunityComment;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final PublicPostService publicPostService;
    private final MeetPostService meetPostService;

    public CommentResponseDto postPublicComment(Long postId, CommentRequestDto commentRequestDto) {
        PublicPost publicPost = publicPostService.findById(postId);
        CommunityComment communityComment = new CommunityComment(publicPost,commentRequestDto);
        commentRepository.save(communityComment);
        return new CommentResponseDto(communityComment);
    }

    public void deletePublicComment(Long commentId) {
        CommunityComment communityComment = findById(commentId);
        commentRepository.delete(communityComment);
    }

    public CommentResponseDto postMeetComment(Long postId, CommentRequestDto commentRequestDto) {
        MeetPost meetPost = meetPostService.findById(postId);
        CommunityComment communityComment = new CommunityComment(meetPost,commentRequestDto);
        commentRepository.save(communityComment);
        return new CommentResponseDto(communityComment);
    }

    public void deleteMeetComment(Long commentId) {
        CommunityComment communityComment = findById(commentId);
        commentRepository.delete(communityComment);
    }

    public CommunityComment findById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
