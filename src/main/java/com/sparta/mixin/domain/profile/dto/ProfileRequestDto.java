package com.sparta.mixin.domain.profile.dto;

import com.sparta.mixin.domain.profile.entity.Interest;
import com.sparta.mixin.domain.profile.entity.ParticipationType;
import com.sparta.mixin.domain.profile.entity.Personality;
import com.sparta.mixin.domain.profile.entity.ValueSystem;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProfileRequestDto {
    @NotNull
    private ParticipationType participationType;
    @NotNull
    private Personality personality;
    @NotNull
    private Interest interest;
    @NotNull
    private ValueSystem valueSystem;
    @NotNull
    private String profileImage;
    @NotNull
    private String shortIntro;
}
