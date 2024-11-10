package com.sparta.mixin.domain.user.service;

import com.sparta.mixin.domain.auth.dto.TokenResponseDto;
import com.sparta.mixin.global.jwt.JwtUtil;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.entity.UserRepository;
import com.sparta.mixin.domain.user.entity.UserRoleEnum;
import com.sparta.mixin.domain.user.entity.UserStatus;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    public void logout(User user) {
        User finduser = findByUsername(user.getUsername());
        checkUserStatus(finduser);
        finduser.updateRefresh("");
    }

    @Transactional
    public void withdraw(User user) {
        User finduser = findByUsername(user.getUsername());
        checkUserStatus(finduser);
        finduser.updateRefresh("");
        finduser.updateStatus(UserStatus.LEAVE);
    }

    @Transactional
    public TokenResponseDto refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);
        // 토큰 유효성 및 만료 확인
        if (!jwtUtil.validateToken(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = claims.getSubject();
        UserRoleEnum role = jwtUtil.getRoleFromToken(refreshToken);

        User user = findByUsername(username);
        String userRefreshToken = user.getRefreshToken().replace("Bearer ", "");

        if (!refreshToken.equals(userRefreshToken)) {
            throw new CustomException(ErrorCode.UNMATCHED_TOKEN);
        }

        TokenResponseDto newToken = jwtUtil.createToken(username, role);
        user.updateRefresh(newToken.getRefreshToken());
        return new TokenResponseDto(newToken.getAccessToken(), newToken.getRefreshToken());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

    private void checkUserStatus(User user) {
        if (!user.isExist()) {
            throw new CustomException(ErrorCode.WITHDRAW_USER);
        }
    }
}
