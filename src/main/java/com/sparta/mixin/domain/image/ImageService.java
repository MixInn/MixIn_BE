package com.sparta.mixin.domain.image;

import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

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
            // 저장할 경로를 설정
            String uploadDirectory = "uploads/images/";
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory + fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 경로를 DB에 저장 (로직은 아래에서 설명)
            String fileUrl = "/uploads/images/" + fileName;

            return fileUrl;
        } catch (IOException e) {
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
}

