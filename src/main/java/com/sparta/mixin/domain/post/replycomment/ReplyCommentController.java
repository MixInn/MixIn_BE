package com.sparta.mixin.domain.post.replycomment;

import com.sparta.mixin.domain.post.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.comment.dto.ReplyCommentResponseDto;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/reply")
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    @PostMapping("/{commentId}")
    public ResponseEntity<CommonResponse<ReplyCommentResponseDto>> postReplyComment(@PathVariable(name = "commentId")Long commentId,@RequestBody
        CommentRequestDto commentRequestDto,@AuthenticationPrincipal
        UserDetailsImpl userDetails){
        ReplyCommentResponseDto responseDto = replyCommentService.postReplyComment(commentId,commentRequestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse("대댓글 작성 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{replyCommentId}")
    public ResponseEntity<CommonResponse<ReplyCommentResponseDto>> editReplyComment(@PathVariable(name = "replyCommentId")Long replyCommentId,@RequestBody
    CommentRequestDto commentRequestDto,@AuthenticationPrincipal
    UserDetailsImpl userDetails){
        ReplyCommentResponseDto responseDto = replyCommentService.editReplyComment(replyCommentId,commentRequestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse("대댓글 수정 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{replyCommentId}")
    public ResponseEntity<CommonResponse> deleteReplyComment(@PathVariable(name = "replyCommentId")Long replyCommentId,
    @AuthenticationPrincipal
    UserDetailsImpl userDetails){
        replyCommentService.deleteReplyComment(replyCommentId,userDetails.getUser());
        CommonResponse response = new CommonResponse("대댓글 삭제 성공",200,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommonResponse<List<ReplyCommentResponseDto>>> getAllReplyComment(@PathVariable(name = "commentId")Long commentId,@AuthenticationPrincipal
    UserDetailsImpl userDetails){
        List<ReplyCommentResponseDto> responseDto = replyCommentService.getAllReplyComment(commentId,userDetails.getUser());
        CommonResponse response = new CommonResponse("대댓글 전체 조회 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
