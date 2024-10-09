package com.sparta.mixin.domain.community.bookmark;

import com.sparta.mixin.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/public/{postId}")
    public ResponseEntity<CommonResponse> postPublicBookmark(@PathVariable(name = "postId")Long postId){
        bookmarkService.postPublicBookmark(postId);
        CommonResponse response = new CommonResponse("공용커뮤니티 북마크 신청 성공",200,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/public/{bookmarkId}")
    public ResponseEntity<CommonResponse> deletePublicBookmark(@PathVariable(name = "bookmarkId")Long bookmarkId){
        bookmarkService.deletePublicBookmark(bookmarkId);
        CommonResponse response = new CommonResponse("공용커뮤니티 북마크 취소 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse> postMeetBookmark(@PathVariable(name = "postId")Long postId){
        bookmarkService.postMeetBookmark(postId);
        CommonResponse response = new CommonResponse("밋커뮤니티 북마크 신청 성공",200,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/meet/{bookmarkId}")
    public ResponseEntity<CommonResponse> deleteMeetBookmark(@PathVariable(name = "bookmarkId")Long bookmarkId){
        bookmarkService.deleteMeetBookmark(bookmarkId);
        CommonResponse response = new CommonResponse("밋커뮤니티 북마크 취소 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
