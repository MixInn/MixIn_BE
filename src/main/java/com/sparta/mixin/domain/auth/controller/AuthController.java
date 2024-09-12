package com.sparta.mixin.domain.auth.controller;

import com.sparta.mixin.domain.auth.dto.SmsRequestDto;
import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.auth.service.SmsService;
import com.sparta.mixin.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

	private final AuthService authService;
	private final SmsService smsService;

	@PostMapping("/signup/sms")
	public ResponseEntity<CommonResponse<String>> sendSms(@Valid @RequestBody SmsRequestDto smsRequestDto) {
		smsService.sendSms(smsRequestDto);
		CommonResponse<String> response = new CommonResponse<>("인증번호가 발송되었습니다.", 200, null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
