package com.sparta.mixin.domain.post.replycomment;

import com.sparta.mixin.domain.post.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.comment.dto.CommentResponseDto;
import com.sparta.mixin.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;

    public CommentResponseDto postReplyComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
    }

    public CommentResponseDto editReplyComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
    }

    public void deleteReplyComment(Long replyCommentId, User user) {
    }

    public List<CommentResponseDto> getAllReplyComment(Long commentId, User user) {
    }
}
