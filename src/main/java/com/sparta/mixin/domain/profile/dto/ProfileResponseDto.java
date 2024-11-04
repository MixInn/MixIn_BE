package com.sparta.mixin.domain.profile.dto;

import com.sparta.mixin.domain.profile.entity.Interest;
import com.sparta.mixin.domain.profile.entity.ParticipationType;
import com.sparta.mixin.domain.profile.entity.Personality;
import com.sparta.mixin.domain.profile.entity.ValueSystem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private String userName;
    private ParticipationType participationType;
    private Personality personality;
    private Interest interest;
    private ValueSystem valueSystem;
    private String profileImage;
    private String shortIntro;

    public ProfileResponseDto(Long id, String userName, ParticipationType participationType,
                              Personality personality, Interest interest, ValueSystem valueSystem,
                              String profileImage, String shortIntro) {
        this.id = id;
        this.userName = userName;
        this.participationType = participationType;
        this.personality = personality;
        this.interest = interest;
        this.valueSystem = valueSystem;
        this.profileImage = profileImage;
        this.shortIntro = shortIntro;
    }
}
