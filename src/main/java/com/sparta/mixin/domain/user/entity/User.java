package com.sparta.mixin.domain.user.entity;

import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String school;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birth;
    private String studentNumber;
    private String major;
    private String email;
    private String password;
    private String telecom;

    // Getters and setters
}

