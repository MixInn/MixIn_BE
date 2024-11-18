package com.sparta.mixin.domain.user.dto;

import com.sparta.mixin.domain.profile.entity.Profile;
import com.sparta.mixin.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String major;
    private String profileImageUrl;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.major = user.getMajor();
        this.profileImageUrl = getProfileImageUrl(user.getProfile());
    }

    private String getProfileImageUrl(Profile profile) {
        return (profile != null) ? profile.getProfileImage() : null;
    }
}