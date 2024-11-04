package com.sparta.mixin.domain.image;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.community.meetpost.MeetPostService;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.PublicPostService;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.image.entity.EntityType;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final ImageRepository imageRepository;
    private final AuthService authService;
    private final MeetPostService meetPostService;
    private final PublicPostService publicPostService;

    public void validateFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String fileExtension = getFileExtension(filename).toLowerCase();
        long fileSize = file.getSize();

        // 이미지 파일 형식 및 크기 제한
        if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals(
            "png")) {
            if (fileSize > 10 * 1024 * 1024) { // 10MB 제한
                throw new CustomException(ErrorCode.NOT_ALLOW_IMAGE_SIZE);
            }
        }
        // 비디오 및 GIF 파일 형식 및 크기 제한
        else if (fileExtension.equals("mp4") || fileExtension.equals("avi") || fileExtension.equals(
            "gif")) {
            if (fileSize > 200 * 1024 * 1024) { // 200MB 제한
                throw new CustomException(ErrorCode.NOT_ALLOW_VIDEO_SIZE);
            }
        } else {
            throw new CustomException(ErrorCode.NOT_ALLOW_FORMAT);
        }
    }

    public String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new CustomException(ErrorCode.INCORRECT_FILE_NAME);
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            throw new CustomException(ErrorCode.INCORRECT_EXTENSION);
        }
        return filename.substring(dotIndex + 1);
    }

    public String getFileUrl(@RequestPart("files") MultipartFile file){
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory + fileName);

            // 디렉토리 존재 여부 확인 및 생성
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 경로 반환
            String fileUrl = uploadDirectory + fileName;
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed.";
        }
    }

    public String deleteFile(String fileUrl) {
        try {
            // 서버의 실제 경로 (DB에 저장된 경로를 기반으로 함)
            Path filePath = Paths.get(fileUrl);

            // 파일이 존재하는지 확인 후 삭제
            Files.deleteIfExists(filePath);

            return "File deleted successfully.";
        } catch (IOException e) {
            return "File deletion failed: " + e.getMessage();
        }
    }

    public List<ImageResponseDto> getAllPostImages(Long postId, String entityType, User user) {
        authService.findByUsername(user.getUsername());
        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();

        if(entityType.equals("MEETPOST")){
            MeetPost meetPost = meetPostService.findById(postId);
            List<Image> imageList = imageRepository.findAllByMeetPost(meetPost);

            for (Image image : imageList) {
                ImageResponseDto imageResponseDto = new ImageResponseDto(image);
                imageResponseDtos.add(imageResponseDto);
            }

        }if(entityType.equals("PUBLICPOST")){
            PublicPost publicPost = publicPostService.findById(postId);
            List<Image> imageList = imageRepository.findAllByPublicPost(publicPost);

            for (Image image : imageList) {
                ImageResponseDto imageResponseDto = new ImageResponseDto(image);
                imageResponseDtos.add(imageResponseDto);
            }
        }
        return imageResponseDtos;
    }

    public void deletePostImage(Long imageId, User user) {
        Image image = imageRepository.findById(imageId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
        User loginUser = authService.findByUsername(user.getUsername());

        if(image.getEntityType().equals(EntityType.MEETPOST)){
            MeetPost meetPost = meetPostService.findById(image.getMeetPost().getId());
            if(meetPost.getUser()!=loginUser){
                throw new CustomException(ErrorCode.NOT_SAME_USER);
            }
        }if(image.getEntityType().equals(EntityType.PUBLICPOST)){
            PublicPost publicPost = publicPostService.findById(image.getPublicPost().getId());
            if(publicPost.getUser()!=loginUser){
                throw new CustomException(ErrorCode.NOT_SAME_USER);
            }
        }

        // 서버에 저장된 이미지 삭제
        deleteFile(image.getImageUrl());

        // DB에 저장된 이미지 삭제
        imageRepository.delete(image);
    }
}

