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

	// Token
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
	TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 재로그인 해주세요."),
	NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
	FALSE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 리프레쉬 토큰입니다."),
	TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 잘못되었거나 누락되어 있습니다."),
	UNMATCHED_TOKEN(HttpStatus.BAD_REQUEST, "일치하지 않는 토큰입니다."),

	// Auth
	DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
	INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	WITHDRAW_USER(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),
	USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록되지 않은 사용자입니다."),
	HEADER_NOT_FOUND_REFRESH(HttpStatus.BAD_REQUEST,"헤더에 리프레쉬 토큰이 누락되었습니다."),

	// SMS 인증
	INCORRECT_CERTIFICATIONCODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),

	// Email 인증
	INCORRECT_AUTHCODE(HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다."),
	DUPLICATE_AUTHCODE(HttpStatus.BAD_REQUEST,"인증번호가 존재합니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 아이디가 존재하는 이메일 입니다."),

	// Image
	NOT_ALLOW_IMAGE_SIZE(HttpStatus.BAD_REQUEST,"이미지 파일은 최대 10MB까지 업로드 가능합니다"),
	NOT_ALLOW_VIDEO_SIZE(HttpStatus.BAD_REQUEST,"비디오 및 GIF 파일은 최대 200MB까지 업로드 가능합니다."),
	NOT_ALLOW_FORMAT(HttpStatus.BAD_REQUEST,"허용되지 않는 파일 형식입니다."),
	INCORRECT_FILE_NAME(HttpStatus.BAD_REQUEST,"파일 이름이 유효하지 않습니다."),
	INCORRECT_EXTENSION(HttpStatus.BAD_REQUEST,"파일 확장자를 찾을 수 없습니다.");

    private final HttpStatus status;
	private final String message;
}
