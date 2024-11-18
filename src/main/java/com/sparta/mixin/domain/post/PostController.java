package com.sparta.mixin.domain.post;

import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.dto.PostResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> getPublicPost(
        @PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto responseDto = postService.getPost(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("단건 글 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> editPublicPost(
        @PathVariable(name = "postId") Long postId,
        @RequestPart("requestDto") PostRequestDto postRequestDto,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> fileUrls = new ArrayList<>();

        if(files !=null && !files.isEmpty()){
            for (MultipartFile file : files) {
                imageService.validateFile(file);
                String fileUrl = imageService.getFileUrl(file);
                fileUrls.add(fileUrl);
            }
        }
        PostResponseDto responseDto = postService.editPost(postId, postRequestDto, fileUrls,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("글 수정 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deletePublicPost(
        @PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails.getUser());
        CommonResponse response = new CommonResponse("글 삭제 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
