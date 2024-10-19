package com.sparta.mixin.domain.auth.dto;

import com.sparta.mixin.domain.user.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequestDto {

    @NotBlank(message = "사용자 Email은 필수 입력 사항입니다.")
    @Email(message = "정확한 이메일 주소를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(az, AZ), 숫자(0~9),특수문자로 구성되어야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "학번을 입력해주세요.")
    private String studentId;

    @NotBlank(message = "대학명을 입력하세요.")
    private String university;

    @NotBlank(message = "학과명을 입력하세요.")
    private String major;

    @NotBlank(message = "휴대폰 번호를 입력하세요.")
    private String phoneNumber;

    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;

}
