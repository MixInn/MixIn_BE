package com.sparta.mixin.domain.user.entity;

import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String studentId;
    private String university;
    private String major;
    private String name;
    private String password;
    private String image;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "refresh_token",nullable = false)
    private String refreshToken;

    public void updateRefresh(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isExist() {
        return this.userStatus == UserStatus.NORMAL;
    }

    @Builder
    public User (
            String username, String phoneNumber, Gender gender, String studentId, String university,
            String major, String name, String password, UserRoleEnum role, UserStatus userStatus,
            String refreshToken) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.studentId = studentId;
        this.university = university;
        this.major = major;
        this.name = name;
        this.password = password;
        this.role = role;
        this.userStatus = userStatus;
        this.refreshToken = refreshToken;
    }
}

