package com.sparta.mixin.domain.image;

import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}

