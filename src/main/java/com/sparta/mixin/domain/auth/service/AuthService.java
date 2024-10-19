package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.SignupRequestDto;
import com.sparta.mixin.domain.auth.dto.TokenResponseDto;
import com.sparta.mixin.domain.auth.repository.UserRepository;
import com.sparta.mixin.domain.jwt.JwtUtil;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.entity.UserRoleEnum;
import com.sparta.mixin.domain.user.entity.UserStatus;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUser =  userRepository.findByUsername(username);

        if (checkUser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        UserRoleEnum role = UserRoleEnum.USER;
//        if (requestDto.isManager()) {
//            if (!MANAGER_TOKEN.equals(requestDto.getManagerToken())) {
//                throw new CustomException(ErrorEnum.BAD_MANAGER_TOKEN);
//            }
//            role = Role.MANAGER;
//        }
        User user = User.builder()
                .username(username)
                .password(password)
                .name(requestDto.getName())
                .studentId(requestDto.getStudentId())
                .university((requestDto.getUniversity()))
                .major(requestDto.getMajor())
                .phoneNumber(requestDto.getPhoneNumber())
                .gender(requestDto.getGender())
                .userStatus(UserStatus.NORMAL)
                .role(role)
                .refreshToken("")
                .build();

        userRepository.save(user);
    }

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
