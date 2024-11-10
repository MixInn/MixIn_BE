package com.sparta.mixin.domain.post.bookmark;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommonResponse> postPublicBookmark(
        @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        bookmarkService.postBookmark(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("북마크 신청 성공", 200, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deletePublicBookmark(
        @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        bookmarkService.deleteBookmark(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("북마크 취소 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
