package com.sparta.mixin.domain.auth.controller;

import com.sparta.mixin.domain.auth.dto.*;
import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.auth.service.MailService;
import com.sparta.mixin.domain.auth.service.SmsService;
import com.sparta.mixin.domain.auth.service.UniversityInfoService;
import com.sparta.mixin.global.common.CommonResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final SmsService smsService;
	private final MailService mailService;
	private final UniversityInfoService universityInfoService;
	private final AuthService authService;


	@PostMapping("/sms")
	public ResponseEntity<CommonResponse<String>> sendSms(@Valid @RequestBody SmsRequestDto requestDto) {
		smsService.sendSms(requestDto);
		CommonResponse<String> response = new CommonResponse<>("인증번호가 발송되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/sms/verify")
	public ResponseEntity<CommonResponse<String>> verifySms(@Valid @RequestBody SmsCertificationRequestDto requestDto) {
		smsService.verifySms(requestDto);
		CommonResponse<String> response = new CommonResponse<>("인증번호 검증이 완료 되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/university/search")
	public ResponseEntity<CommonResponse<String>> searchUniversities(@RequestParam int page, @RequestParam int perPage) {
		String searchResult = universityInfoService.getUniversities(page, perPage);
		CommonResponse<String> response = new CommonResponse<>("대학교/학과 검색이 성공적으로 완료 되었습니다.", 200, searchResult);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/email")
	public ResponseEntity<CommonResponse<EmailAddressDto>> sendAuthenticationCode(
		@RequestBody @Valid EmailAddressDto emailAddressDto
	) throws MessagingException {
		EmailAddressDto addressDto = mailService.sendAuthenticationCode(emailAddressDto.getEmail());
		CommonResponse<EmailAddressDto> response = new CommonResponse<>("이메일 인증번호 전송 완료되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/email/verify")
	public ResponseEntity<CommonResponse<String>> emailAuthentication(
		@RequestBody @Valid EmailAuthenticationDto emailAuthenticationDto
	) {
		mailService.emailAuthentication(emailAuthenticationDto.getEmail(), emailAuthenticationDto.getCode());
		CommonResponse<String> response = new CommonResponse<>("이메일 인증번호 검증이 완료되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<CommonResponse<String>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		authService.signup(signupRequestDto);
		CommonResponse<String> response = new CommonResponse<>("회원가입이 완료되었습니다.", 201, null);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/logout")
	public ResponseEntity<CommonResponse<String>> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		authService.logout(userDetails.getUser());
		CommonResponse<String> response = new CommonResponse<>("로그아웃이 완료되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<CommonResponse<String>> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		authService.withdraw(userDetails.getUser());
		CommonResponse<String> response = new CommonResponse<>("회원탈퇴가 완료되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<CommonResponse<TokenResponseDto>> refreshToken(HttpServletRequest request) {
		TokenResponseDto token = authService.refreshToken(request);
		CommonResponse<TokenResponseDto> response = new CommonResponse<>("리프레쉬 토큰 발급이 완료되었습니다.", 200, token);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
