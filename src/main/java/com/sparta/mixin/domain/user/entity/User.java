package com.sparta.mixin.domain.user.entity;

import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String studentId;
    private String university;
    private String major;
    private String email; // 아이디로 사용될 이메일
    private String password;

}

