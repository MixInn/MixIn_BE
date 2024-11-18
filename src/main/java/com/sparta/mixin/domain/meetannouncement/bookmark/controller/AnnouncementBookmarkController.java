package com.sparta.mixin.domain.meetannouncement.bookmark.controller;

import com.sparta.mixin.domain.meetannouncement.bookmark.service.AnnouncementBookmarkService;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet/announcement")
@RequiredArgsConstructor
public class AnnouncementBookmarkController {
    private final AnnouncementBookmarkService announcementBookmarkService;

    @PostMapping("/{announcementId}/bookmark")
    public ResponseEntity<CommonResponse<Void>> addBookmark(
            @PathVariable Long announcementId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        announcementBookmarkService.addBookmark(announcementId, userDetails.getUser());
        return ResponseEntity.ok(new CommonResponse<>("북마크 추가 성공", 200, null));
    }

    @DeleteMapping("/{announcementId}/bookmark")
    public ResponseEntity<CommonResponse<Void>> removeBookmark(
            @PathVariable Long announcementId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        announcementBookmarkService.removeBookmark(announcementId, userDetails.getUser());
        return ResponseEntity.ok(new CommonResponse<>("북마크 삭제 성공", 200, null));
    }
}
