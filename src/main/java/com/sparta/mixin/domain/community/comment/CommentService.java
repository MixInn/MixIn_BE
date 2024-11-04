package com.sparta.mixin.domain.community.comment;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.community.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.community.comment.dto.CommentResponseDto;
import com.sparta.mixin.domain.community.comment.entity.CommunityComment;
import com.sparta.mixin.domain.community.meetpost.MeetPostService;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.PublicPostService;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PublicPostService publicPostService;
    private final MeetPostService meetPostService;
    private final AuthService authService;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public CommentResponseDto postPublicComment(Long postId, CommentRequestDto commentRequestDto,
        User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityComment communityComment = new CommunityComment(publicPost,commentRequestDto,loginUser);
        commentRepository.save(communityComment);
        return new CommentResponseDto(communityComment);
    }

    public void deletePublicComment(Long commentId, User user) {
        CommunityComment communityComment = findById(commentId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(communityComment.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        commentRepository.delete(communityComment);
    }

    public List<CommentResponseDto> getPublicComment(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        authService.findByUsername(user.getUsername());

        List<CommunityComment> publicCommentList = commentRepository.findAllByPublicPost(publicPost);

        return publicCommentList.stream().map(CommentResponseDto::new).toList();
    }

    public CommentResponseDto postMeetComment(Long postId, CommentRequestDto commentRequestDto,
        User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        Meet meet = meetService.findById(meetPost.getMeet().getId());
        if(meetAuthorizationService.findByMeetAndUser(meet,loginUser)==null){
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }

        CommunityComment communityComment = new CommunityComment(meetPost,commentRequestDto,loginUser);
        commentRepository.save(communityComment);
        return new CommentResponseDto(communityComment);
    }

    public void deleteMeetComment(Long commentId, User user) {
        CommunityComment communityComment = findById(commentId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(communityComment.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        commentRepository.delete(communityComment);
    }

    public List<CommentResponseDto> getMeetComment(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        authService.findByUsername(user.getUsername());

        List<CommunityComment> meetCommentList = commentRepository.findAllByMeetPost(meetPost);

        return meetCommentList.stream().map(CommentResponseDto::new).toList();
    }

    public CommunityComment findById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
