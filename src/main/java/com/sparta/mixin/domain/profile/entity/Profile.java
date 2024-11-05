package com.sparta.mixin.domain.profile.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profile")
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ParticipationType participationType;

    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Enumerated(EnumType.STRING)
    private Interest interest;

    @Enumerated(EnumType.STRING)
    private ValueSystem valueSystem;

    @Column(nullable = true)
    private String profileImage;

    private String shortIntro;

    public Profile(ParticipationType participationType, Personality personality,
            Interest interest, ValueSystem valueSystem, String profileImage, String shortIntro) {
        this.participationType = participationType;
        this.personality = personality;
        this.interest = interest;
        this.valueSystem = valueSystem;
        this.profileImage = profileImage;
        this.shortIntro = shortIntro;
    }

    public void addPofileImage(String fileUrl) {
        this.profileImage = fileUrl;
    }
}
