package com.sparta.mixin.domain.post.publicpost;

import com.sparta.mixin.global.security.UserDetailsImpl;
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
@RequestMapping("/community/public")
public class PublicPostController {

    private final PublicPostService publicPostService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<CommonResponse<PostResponseDto>> createPublicPost(
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

        PostResponseDto responseDto = publicPostService.createPost(postRequestDto, "PUBLICPOST",
            fileUrls, userDetails.getUser(), null);
        CommonResponse response = new CommonResponse("공용커뮤니티에 글 작성 성공", 201, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<PostResponseDto>>> getAllPublicPost(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam String orderBy,
        @RequestParam String searchWord,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<? extends PostResponseDto> responseDtos = publicPostService.getAllPost(page - 1,
            size,orderBy, searchWord,"PUBLICPOST", userDetails.getUser(), null);
        CommonResponse response = new CommonResponse("공용커뮤니티 글 전체 조회 성공", 200, responseDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
