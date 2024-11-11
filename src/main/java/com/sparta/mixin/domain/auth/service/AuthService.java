package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.SignupRequestDto;
import com.sparta.mixin.domain.user.entity.UserRepository;
import com.sparta.mixin.global.jwt.JwtUtil;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.entity.UserRoleEnum;
import com.sparta.mixin.domain.user.entity.UserStatus;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
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

    @Transactional
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

}
