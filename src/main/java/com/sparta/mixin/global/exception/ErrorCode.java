package com.sparta.mixin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Basic HttpStatusCode
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
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

	// SMS
	INCORRECT_CERTIFICATIONCODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),

	// Email
	INCORRECT_AUTHCODE(HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다."),
	DUPLICATE_AUTHCODE(HttpStatus.BAD_REQUEST,"인증번호가 존재합니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 아이디가 존재하는 이메일 입니다."),

	// User
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT FOUND USER"),
	SAME_USER_PRODUCT(HttpStatus.BAD_REQUEST, "SAME USER PRODUCT"),
	NOT_SAME_USER(HttpStatus.BAD_REQUEST, "동일한 사용자가 아닙니다."),

	// Image
	NOT_ALLOW_IMAGE_SIZE(HttpStatus.BAD_REQUEST,"이미지 파일은 최대 10MB까지 업로드 가능합니다"),
	NOT_ALLOW_VIDEO_SIZE(HttpStatus.BAD_REQUEST,"비디오 및 GIF 파일은 최대 200MB까지 업로드 가능합니다."),
	NOT_ALLOW_FORMAT(HttpStatus.BAD_REQUEST,"허용되지 않는 파일 형식입니다."),
	INCORRECT_FILE_NAME(HttpStatus.BAD_REQUEST,"파일 이름이 유효하지 않습니다."),
	INCORRECT_EXTENSION(HttpStatus.BAD_REQUEST,"파일 확장자를 찾을 수 없습니다."),
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하였습니다."),
	FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 실패하였습니다."),

	// Post
	INVALID_POST_TYPE(HttpStatus.BAD_REQUEST,"유효하지 않은 포스트 타입입니다."),
	MISSING_MEET_ID(HttpStatus.BAD_REQUEST,"밋 아이디를 입력해주세요."),

	// Bookmark
	ALREADY_REGISTERED_BOOKMARK(HttpStatus.BAD_REQUEST,"이미 등록된 북마크입니다."),
	NOT_EXISTING_BOOKMARK(HttpStatus.BAD_REQUEST,"존재하지 않는 북마크입니다."),

	// Like
	ALREADY_REGISTERED_Like(HttpStatus.BAD_REQUEST,"이미 등록된 좋아요입니다."),
	NOT_EXISTING_Like(HttpStatus.BAD_REQUEST,"존재하지 않는 좋아요입니다."),

	// Meet
	MEET_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),

	// meetAnnouncement
	MEET_ANNOUNCEMENT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 모임 공고입니다."),
	MEET_ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임 공고입니다."),

	// meetApplication
	ALREADY_APPLIED(HttpStatus.BAD_REQUEST,"이미 모임신청하였습니다."),

	// meetActivity
	ACTIVITY_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 활동입니다."),
	PARTICIPATION_NOT_FOUND(HttpStatus.BAD_REQUEST,"활동에 참석하지 않은 사용자입니다."),
	ALREADY_PARTICIPATING(HttpStatus.BAD_REQUEST,"이미 활동에 참석하였습니다."),
	// 밋권한 관련
	UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
	INCORRECT_MEET_USER(HttpStatus.UNAUTHORIZED, "해당 밋에 소속된 사용자가 아닙니다.");

    private final HttpStatus status;
	private final String message;
}
