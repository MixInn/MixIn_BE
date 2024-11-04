package com.sparta.mixin.domain.profile.entity;

import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Table(name = "profile")
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public Profile(User user, ParticipationType participationType, Personality personality,
            Interest interest, ValueSystem valueSystem, String profileImage, String shortIntro) {
        this.user = user;
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
