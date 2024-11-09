package com.sparta.mixin.domain.post.meetpost;

import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.dto.PostResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/meet")
public class MeetPostController {

    private final MeetPostService meetPostService;
    private final ImageService imageService;

    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> createMeetPost(
        @PathVariable(name = "meetId") Long meetId,
        @RequestPart("requestDto") PostRequestDto postRequestDto,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            imageService.validateFile(file);
            String fileUrl = imageService.getFileUrl(file);
            fileUrls.add(fileUrl);
        }

        PostResponseDto responseDto = meetPostService.createPost(postRequestDto, "MEETPOST",fileUrls, userDetails.getUser(),meetId);
        CommonResponse response = new CommonResponse("밋커뮤니티에 글 작성 성공", 201, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> getMeetPost(
        @PathVariable(name = "postId") Long postId,@AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        PostResponseDto responseDto = meetPostService.getPost(postId,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("밋커뮤니티 단건 글 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> editMeetPost(
        @PathVariable(name = "postId") Long postId,
        @RequestPart("requestDto") PostRequestDto postRequestDto,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            imageService.validateFile(file);
            String fileUrl = imageService.getFileUrl(file);
            fileUrls.add(fileUrl);
        }

        PostResponseDto responseDto = meetPostService.editPost(postId, postRequestDto,fileUrls,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("밋커뮤니티 글 수정 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deleteMeetPost(
        @PathVariable(name = "postId") Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        meetPostService.deletePost(postId,userDetails.getUser());
        CommonResponse response = new CommonResponse("밋커뮤니티 글 삭제 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{meetId}/all")
    public ResponseEntity<CommonResponse<Page<PostResponseDto>>> getAllMeetPost(
        @PathVariable(name = "meetId") Long meetId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        Page<PostResponseDto> responseDtos = meetPostService.getAllPost(page - 1,
            size,"MEETPOST",userDetails.getUser(),meetId);
        CommonResponse response = new CommonResponse("밋커뮤니티 글 전체 조회 성공", 200, responseDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
