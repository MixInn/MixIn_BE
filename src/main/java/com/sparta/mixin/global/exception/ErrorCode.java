package com.sparta.mixin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Basic HttpStatusCode
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),

	// 각 Service에서 필요한 ErrorCode 추가
	// Image
	NOT_ALLOW_IMAGE_SIZE(HttpStatus.BAD_REQUEST,"이미지 파일은 최대 10MB까지 업로드 가능합니다"),
	NOT_ALLOW_VIDEO_SIZE(HttpStatus.BAD_REQUEST,"비디오 및 GIF 파일은 최대 200MB까지 업로드 가능합니다."),
	NOT_ALLOW_FORMAT(HttpStatus.BAD_REQUEST,"허용되지 않는 파일 형식입니다."),
	INCORRECT_FILE_NAME(HttpStatus.BAD_REQUEST,"파일 이름이 유효하지 않습니다."),
	INCORRECT_EXTENSION(HttpStatus.BAD_REQUEST,"파일 확장자를 찾을 수 없습니다.");

    private final HttpStatus status;
	private final String message;
}
