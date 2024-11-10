package com.sparta.mixin.domain.post.like;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommonResponse> postLike(@PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        likeService.postLike(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("좋아요 신청 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deleteLike(
        @PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.deleteLike(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("좋아요 취소 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
