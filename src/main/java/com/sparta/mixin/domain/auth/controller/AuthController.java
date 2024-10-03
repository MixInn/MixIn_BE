package com.sparta.mixin.domain.auth.controller;

import com.sparta.mixin.domain.auth.dto.SmsCertificationRequestDto;
import com.sparta.mixin.domain.auth.dto.SmsRequestDto;
import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.auth.service.SmsService;
import com.sparta.mixin.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final AuthService authService;
	private final SmsService smsService;

	@PostMapping("/signup/sms")
	public ResponseEntity<CommonResponse<String>> sendSms(@Valid @RequestBody SmsRequestDto requestDto) {
		smsService.sendSms(requestDto);
		CommonResponse<String> response = new CommonResponse<>("인증번호가 발송되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/signup/sms/verify")
	public ResponseEntity<CommonResponse<String>> verifySms(@Valid @RequestBody SmsCertificationRequestDto requestDto) {
		smsService.verifySms(requestDto);
		CommonResponse<String> response = new CommonResponse<>("인증번호 검증이 완료 되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/university/search")
	public ResponseEntity<CommonResponse<String>> searchUniversities(@RequestParam int page, @RequestParam int perPage) {
		String searchResult = authService.getUniversities(page, perPage);
		CommonResponse<String> response = new CommonResponse<>("대학교/학과 검색이 성공적으로 완료 되었습니다.", 200, searchResult);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
