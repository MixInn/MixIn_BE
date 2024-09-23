package com.sparta.mixin.domain.community.publicpost;

import com.sparta.mixin.domain.community.publicpost.dto.PublicPostRequestDto;
import com.sparta.mixin.domain.community.publicpost.dto.PublicPostResponseDto;
import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.global.common.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<CommonResponse<PublicPostResponseDto>> createPublicPost(@RequestBody PublicPostRequestDto publicPostRequestDto,
        @RequestPart("files") List<MultipartFile> files){
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            imageService.validateFile(file);
            String fileUrl = imageService.getFileUrl(file);
            fileUrls.add(fileUrl);
        }
        PublicPostResponseDto responseDto = publicPostService.createPublicPost(publicPostRequestDto,fileUrls);
        CommonResponse response = new CommonResponse("공용커뮤니티에 글 작성 성공",201,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
