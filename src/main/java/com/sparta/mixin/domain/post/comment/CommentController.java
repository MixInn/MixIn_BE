package com.sparta.mixin.domain.post.comment;

import com.sparta.mixin.domain.post.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.comment.dto.CommentResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postComment(
        @PathVariable(name = "postId") Long postId, @RequestBody
    CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.postComment(postId, commentRequestDto,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("댓글 작성 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(
        @PathVariable(name = "commentId") Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        CommonResponse response = new CommonResponse("댓글 삭제 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> getComment(
        @PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CommentResponseDto> responseDto = commentService.getComment(postId,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("댓글 전체 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
