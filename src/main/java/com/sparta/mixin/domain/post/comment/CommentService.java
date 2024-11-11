package com.sparta.mixin.domain.post.comment;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.comment.dto.CommentResponseDto;
import com.sparta.mixin.domain.post.comment.entity.PostComment;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public CommentResponseDto postComment(Long postId, CommentRequestDto commentRequestDto,
        User user) {
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

        PostComment communityComment = new PostComment(post, loginUser, commentRequestDto);
        commentRepository.save(communityComment);
        post.increaseCommentCount();
        return new CommentResponseDto(communityComment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        PostComment postComment = findById(commentId);
        Post post = postComment.getPost();
        User loginUser = userService.findByUsername(user.getUsername());

        if (postComment.getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        commentRepository.delete(postComment);
        post.decreaseCommentCount();
    }

    public List<CommentResponseDto> getComment(Long postId, User user) {
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

        List<PostComment> publicCommentList = commentRepository.findAllByPost(post);

        return publicCommentList.stream().map(CommentResponseDto::new).toList();
    }

    public PostComment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
