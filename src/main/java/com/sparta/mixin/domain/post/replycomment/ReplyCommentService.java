package com.sparta.mixin.domain.post.replycomment;

import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.PostType;
import com.sparta.mixin.domain.post.comment.CommentService;
import com.sparta.mixin.domain.post.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.comment.dto.CommentResponseDto;
import com.sparta.mixin.domain.post.comment.dto.ReplyCommentResponseDto;
import com.sparta.mixin.domain.post.comment.entity.PostComment;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentService commentService;
    private final PostService postService;
    private final MeetAuthorizationService meetAuthorizationService;
    private final UserService userService;

    public ReplyCommentResponseDto postReplyComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        PostComment postComment = commentService.findById(commentId);
        User loginUser = userService.findByUsername(user.getUsername());

        if(postComment.getPostType().equals(PostType.MEETPOST)){
            MeetPost meetPost = (MeetPost)postComment.getPost();
            if(meetAuthorizationService.findByMeetAndUser(meetPost.getMeet(),loginUser)==null){
                throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
            }
        }
        ReplyComment replyComment = new ReplyComment(postComment,commentRequestDto,loginUser);
        replyCommentRepository.save(replyComment);
        replyComment.getPostComment().getPost().increaseCommentCount();
        postService.save(replyComment.getPostComment().getPost());

        return new ReplyCommentResponseDto(replyComment);
    }

    public ReplyCommentResponseDto editReplyComment(Long replyCommentId, CommentRequestDto commentRequestDto, User user) {
        User loginUser = userService.findByUsername(user.getUsername());
        ReplyComment replyComment = findById(replyCommentId);

        if(replyComment.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        replyComment.updateReplyComment(commentRequestDto);
        replyCommentRepository.save(replyComment);

        return new ReplyCommentResponseDto(replyComment);
    }

    @Transactional
    public void deleteReplyComment(Long replyCommentId, User user) {
        User loginUser = userService.findByUsername(user.getUsername());
        ReplyComment replyComment = findById(replyCommentId);

        if(replyComment.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        replyCommentRepository.delete(replyComment);
        replyComment.getPostComment().getPost().decreaseCommentCount();
        postService.save(replyComment.getPostComment().getPost());
    }

    public List<ReplyCommentResponseDto> getAllReplyComment(Long commentId, User user) {
        PostComment postComment = commentService.findById(commentId);
        User loginUser = userService.findByUsername(user.getUsername());

        if(postComment.getPostType().equals(PostType.MEETPOST)){
            MeetPost meetPost = (MeetPost)postComment.getPost();
            if(meetAuthorizationService.findByMeetAndUser(meetPost.getMeet(),loginUser)==null){
                throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
            }
        }

        List<ReplyComment> replyCommentList = replyCommentRepository.findAllByPostComment(postComment);
        return replyCommentList.stream().map(ReplyCommentResponseDto::new).toList();
    }

    public ReplyComment findById(Long replyCommentId){
        return replyCommentRepository.findById(replyCommentId).orElseThrow(
            ()-> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
