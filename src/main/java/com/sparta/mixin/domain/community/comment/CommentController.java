package com.sparta.mixin.domain.community.comment;

import com.sparta.mixin.domain.community.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.community.comment.dto.CommentResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("community/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/public/{postId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postPublicComment(
        @PathVariable(name = "postId") Long postId, @RequestBody
    CommentRequestDto commentRequestDto) {
        CommentResponseDto responseDto = commentService.postPublicComment(postId,commentRequestDto);
        CommonResponse response = new CommonResponse("공용커뮤니티에 댓글 작성 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deletePublicComment(
        @PathVariable(name = "commentId") Long commentId) {
        commentService.deletePublicComment(commentId);
        CommonResponse response = new CommonResponse("공용커뮤니티에 댓글 삭제 성공",204,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postMeetComment(
        @PathVariable(name = "postId") Long postId, @RequestBody
    CommentRequestDto commentRequestDto) {
        CommentResponseDto responseDto = commentService.postMeetComment(postId,commentRequestDto);
        CommonResponse response = new CommonResponse("밋커뮤니티에 댓글 작성 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deleteMeetComment(
        @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteMeetComment(commentId);
        CommonResponse response = new CommonResponse("밋커뮤니티에 댓글 삭제 성공",204,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
