package com.sparta.mixin.domain.image;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<List<ImageResponseDto>>> getAllPostImages(
        @PathVariable(name = "postId") Long postId,
        @RequestParam("entityType") String entityType,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        List<ImageResponseDto> responseDtos = imageService.getAllPostImages(postId,entityType,userDetails.getUser());
        CommonResponse response = new CommonResponse("커뮤니티 글 이미지 조회 성공", 200, responseDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<CommonResponse> deletePostImage(
        @PathVariable(name = "imageId") Long imageId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        imageService.deletePostImage(imageId,userDetails.getUser());
        CommonResponse response = new CommonResponse("커뮤니티 글 이미지 단건 삭제 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
