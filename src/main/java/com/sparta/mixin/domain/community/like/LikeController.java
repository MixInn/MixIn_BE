package com.sparta.mixin.domain.community.like;

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
@RequestMapping("like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/public/{postId}")
    public ResponseEntity<CommonResponse> postPublicLike(@PathVariable(name = "postId")Long postId){
        likeService.postPublicLike(postId);
        CommonResponse response = new CommonResponse("공용커뮤니티 좋아요 신청 성공",200,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/public/{likekId}")
    public ResponseEntity<CommonResponse> deletePublicLike(@PathVariable(name = "likekId")Long likekId){
        likeService.deletePublicLike(likekId);
        CommonResponse response = new CommonResponse("공용커뮤니티 좋아요 취소 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/meet/{postId}")
    public ResponseEntity<CommonResponse> postMeetLike(@PathVariable(name = "postId")Long postId){
        likeService.postMeetLike(postId);
        CommonResponse response = new CommonResponse("밋커뮤니티 좋아요 신청 성공",200,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/meet/{likekId}")
    public ResponseEntity<CommonResponse> deleteMeetLike(@PathVariable(name = "likekId")Long likekId){
        likeService.deleteMeetLike(likekId);
        CommonResponse response = new CommonResponse("밋커뮤니티 좋아요 취소 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
