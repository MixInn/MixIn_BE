package com.sparta.mixin.domain.community.bookmark;

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
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/public/{postId}")
    public ResponseEntity<CommonResponse> postPublicBookmark(
        @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        bookmarkService.postPublicBookmark(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티 북마크 신청 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/public/{postId}")
    public ResponseEntity<CommonResponse> deletePublicBookmark(
        @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        bookmarkService.deletePublicBookmark(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("공용커뮤니티 북마크 취소 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse> postMeetBookmark(
        @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        bookmarkService.postMeetBookmark(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티 북마크 신청 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse> deleteMeetBookmark(
        @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        bookmarkService.deleteMeetBookmark(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티 북마크 취소 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
