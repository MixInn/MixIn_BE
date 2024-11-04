package com.sparta.mixin.domain.community.like;

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

    @PostMapping("/public/{postId}")
    public ResponseEntity<CommonResponse> postPublicLike(@PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        likeService.postPublicLike(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티 좋아요 신청 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/public/{postId}")
    public ResponseEntity<CommonResponse> deletePublicLike(
        @PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.deletePublicLike(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티 좋아요 취소 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse> postMeetLike(@PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.postMeetLike(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티 좋아요 신청 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse> deleteMeetLike(@PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.deleteMeetLike(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티 좋아요 취소 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
