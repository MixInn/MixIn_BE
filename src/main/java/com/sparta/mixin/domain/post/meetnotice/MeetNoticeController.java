package com.sparta.mixin.domain.post.meetnotice;

import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.dto.PostResponseDto;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meet/notice")
public class MeetNoticeController {

    private final ImageService imageService;
    private final MeetNoticeService meetNoticeService;

    @PostMapping("/{meetId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> createMeetNotice(
        @PathVariable(name = "meetId") Long meetId,
        @RequestPart("requestDto") PostRequestDto postRequestDto,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        List<String> fileUrls = new ArrayList<>();

        if(files !=null && !files.isEmpty()){
            for (MultipartFile file : files) {
                imageService.validateFile(file);
                String fileUrl = imageService.getFileUrl(file);
                fileUrls.add(fileUrl);
            }
        }

        PostResponseDto responseDto = meetNoticeService.createPost(postRequestDto, "MEETNOTICE",
            fileUrls, userDetails.getUser(), meetId);
        CommonResponse response = new CommonResponse("밋공지에 글 작성 성공", 201, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{meetId}/all")
    public ResponseEntity<CommonResponse<Page<PostResponseDto>>> getAllMeetNotice(
        @PathVariable(name = "meetId") Long meetId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String orderBy,
        @RequestParam(defaultValue = "") String searchWord,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        Page<? extends PostResponseDto> responseDtos = meetNoticeService.getAllPost(page - 1,
            size, orderBy,searchWord,"MEETNOTICE", userDetails.getUser(), meetId);
        CommonResponse response = new CommonResponse("밋공지 글 전체 조회 성공", 200, responseDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
