package com.sparta.mixin.domain.user.controller;

import com.sparta.mixin.domain.auth.dto.TokenResponseDto;
import com.sparta.mixin.domain.auth.security.UserDetailsImpl;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userDetails.getUser());
        CommonResponse<String> response = new CommonResponse<>("로그아웃이 완료되었습니다.", 200, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CommonResponse<String>> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(userDetails.getUser());
        CommonResponse<String> response = new CommonResponse<>("회원탈퇴가 완료되었습니다.", 200, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<TokenResponseDto>> refreshToken(HttpServletRequest request) {
        TokenResponseDto token = userService.refreshToken(request);
        CommonResponse<TokenResponseDto> response = new CommonResponse<>("리프레쉬 토큰 발급이 완료되었습니다.", 200, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
