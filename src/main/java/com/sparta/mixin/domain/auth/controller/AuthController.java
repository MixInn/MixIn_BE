package com.sparta.mixin.domain.auth.controller;

import com.sparta.mixin.domain.auth.dto.*;
import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.auth.service.SmsService;
import com.sparta.mixin.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@GetMapping("/search/university")
	public ResponseEntity<CommonResponse<List<UniversityResponseDto>>> searchUniversity(@Valid @RequestBody UniversitySearchRequestDto requestDto) {
		List<UniversityResponseDto> universityList = authService.searchUniversities(requestDto.getUniversityName());
		CommonResponse<List<UniversityResponseDto>> response = new CommonResponse<>("학교 정보 검색이 완료되었습니다.", 200, universityList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search/major")
	public ResponseEntity<CommonResponse<List<MajorResponseDto>>> searchMajor(@Valid @RequestBody MajorSearchRequestDto requestDto) {
		List<MajorResponseDto> majorList = authService.searchMajor(requestDto.getMajor());
		CommonResponse<List<MajorResponseDto>> response = new CommonResponse<>("학과 정보 검색이 완료되었습니다.", 200, majorList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
