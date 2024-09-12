package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.community.meetpost.dto.MeetPostRequestDto;
import com.sparta.mixin.domain.community.meetpost.dto.MeetPostResponseDto;
import com.sparta.mixin.domain.image.ImageService;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<CommonResponse<MeetPostResponseDto>> createMeetPost(@PathVariable(name = "meetId") Long meetId,
        @RequestBody MeetPostRequestDto meetPostRequestDto,
        @RequestPart("files") List<MultipartFile> files){
        String uploadDirectory = "uploads/images/";  // 파일을 저장할 디렉토리
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            imageService.validateFile(file);
            try {
                // 파일명을 고유하게 생성
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDirectory + fileName);

                // 파일을 해당 경로에 저장
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 저장된 파일의 경로 또는 URL을 리스트에 추가
                String fileUrl = "/uploads/images/" + fileName;  // 서버에서 접근 가능한 경로
                fileUrls.add(fileUrl);

            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
        }

        MeetPostResponseDto responseDto = meetPostService.createMeetPost(meetId,
            meetPostRequestDto,fileUrls);
        CommonResponse response = new CommonResponse("밋커뮤니티에 글 작성 성공",201,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<MeetPostResponseDto>> getMeetPost(@PathVariable(name = "postId")Long postId){
        MeetPostResponseDto responseDto = meetPostService.getMeetPost(postId);
        CommonResponse response = new CommonResponse<>("밋커뮤니티 단건 글 조회 성공",200,responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<CommonResponse<MeetPostResponseDto>> editMeetPost(@PathVariable(name = "postId")Long postId,@RequestBody MeetPostRequestDto meetPostRequestDto){
        MeetPostResponseDto responseDto = meetPostService.editMeetPost(postId,meetPostRequestDto);
        CommonResponse response = new CommonResponse<>("밋커뮤니티 글 수정 성공",200,responseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deleteMeetPost(@PathVariable(name = "postId")Long postId){
        meetPostService.deleteMeetPost(postId);
        CommonResponse response = new CommonResponse("밋커뮤니티 글 삭제 성공",204,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{meetId}")
    public ResponseEntity<CommonResponse<Page<MeetPostResponseDto>>> getAllMeetPost(@PathVariable(name = "meetId")Long meetId,
        @RequestParam(defaultValue = "1")int page,
        @RequestParam(defaultValue = "10")int size){
        Page<MeetPostResponseDto> responseDtos = meetPostService.getAllMeetPost(meetId,page-1,size);
        CommonResponse response = new CommonResponse("밋커뮤니티 글 전체 조회 성공",200,responseDtos);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
