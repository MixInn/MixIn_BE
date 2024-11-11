package com.sparta.mixin.domain.community.comment;

import com.sparta.mixin.global.security.UserDetailsImpl;
import com.sparta.mixin.domain.community.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.community.comment.dto.CommentResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
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

    @PostMapping("/public/{postId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postPublicComment(
        @PathVariable(name = "postId") Long postId, @RequestBody
    CommentRequestDto commentRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.postPublicComment(postId,commentRequestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티에 댓글 작성 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/public/{commentId}")
    public ResponseEntity<CommonResponse> deletePublicComment(
        @PathVariable(name = "commentId") Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deletePublicComment(commentId,userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티에 댓글 삭제 성공",204,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/public/{postId}")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> getPublicComment(@PathVariable(name = "postId")Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<CommentResponseDto> responseDto = commentService.getPublicComment(postId,userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티 댓글 조회 성공",200,responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postMeetComment(
        @PathVariable(name = "postId") Long postId, @RequestBody
    CommentRequestDto commentRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.postMeetComment(postId,commentRequestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티에 댓글 작성 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meet/{commentId}")
    public ResponseEntity<CommonResponse> deleteMeetComment(
        @PathVariable(name = "commentId") Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteMeetComment(commentId,userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티에 댓글 삭제 성공",204,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> getMeetComment(@PathVariable(name = "postId")Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<CommentResponseDto> responseDto = commentService.getMeetComment(postId,userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티 댓글 조회 성공",200,responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
