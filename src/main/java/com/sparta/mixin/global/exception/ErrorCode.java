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
	HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "헤더가 잘못되었거나 누락되었습니다."),
	UNMATCHED_TOKEN(HttpStatus.BAD_REQUEST, "일치하지 않는 토큰입니다."),

	// Auth
	DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
	INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	WITHDRAW_USER(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),

	// email
	INCORRECT_AUTHCODE(HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다."),
	DUPLICATE_AUTHCODE(HttpStatus.BAD_REQUEST,"인증번호가 존재합니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 아이디가 존재하는 이메일 입니다."),

	// Product
	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
	NOT_SAME_USER(HttpStatus.BAD_REQUEST, "사용자가 일치하지 않아 요청을 처리할 수 없습니다."),
	ALREADY_SOLD_OUT(HttpStatus.BAD_REQUEST, "이미 판매 완료된 상품입니다."),
	NON_EXISTENT_PRODUCT(HttpStatus.NO_CONTENT, "해당 사용자가 판매하는 상품이 없습니다."),
	NOT_FOUND_IMAGE(HttpStatus.BAD_REQUEST,"이미지를 찾을 수 없습니다."),

	// Cart
	NOT_FOUND_CARTITEM(HttpStatus.NOT_FOUND, "NOT FOUND CART_ITEM"),
	ALREADY_EXIST_CARTITEM(HttpStatus.CONFLICT, "ALREADY_EXIST_CARTITEM"),

	// Orders
	NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "NOT FOUND ORDER"),
	NOT_FOUND_ORDERITEM(HttpStatus.NOT_FOUND, "NOT FOUND ORDERITEM"),
	NOT_FOUND_ORDERSTATUS(HttpStatus.NOT_FOUND, "NOT FOUND ORDERSTATUS"),

	// User
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT FOUND USER"),
	SAME_USER_PRODUCT(HttpStatus.BAD_REQUEST, "SAME USER PRODUCT"),

	// Follow
	SELF_FOLLOW(HttpStatus.BAD_REQUEST,"IDENTICAL_USER"),
	ALREADY_EXIST_FOLLOW(HttpStatus.CONFLICT, "ALREADY_EXIST FOLLOW"),
	NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "NOT FOUND FOLLOW"),
	ALREADY_UNFOLLOWED(HttpStatus.CONFLICT, "ALREADY_UNFOLLOW"),

    // S3
    EXCEED_MAX_COUNT(HttpStatus.BAD_REQUEST,"파일 업로드는 최대 5개까지 허용됩니다."),
    NOT_ALLOW_IMAGE_SIZE(HttpStatus.BAD_REQUEST,"이미지 파일은 최대 10MB까지 업로드 가능합니다"),
    NOT_ALLOW_VIDEO_SIZE(HttpStatus.BAD_REQUEST,"비디오 및 GIF 파일은 최대 200MB까지 업로드 가능합니다."),
    NOT_ALLOW_FORMAT(HttpStatus.BAD_REQUEST,"허용되지 않는 파일 형식입니다."),
    INCORRECT_FILE_NAME(HttpStatus.BAD_REQUEST,"파일 이름이 유효하지 않습니다."),
    INCORRECT_EXTENSION(HttpStatus.BAD_REQUEST,"파일 확장자를 찾을 수 없습니다."),

	// Payment
	NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND,"결제 내역을 찾을 수 없습니다."),
	PAY_AMOUNT_MISMATCH(HttpStatus.CONFLICT, "결제 금액이 일치하지 않습니다.");

    private final HttpStatus status;
	private final String message;
}
