package com.sparta.mixin.domain.meet.entity;

import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meetAuthorization")
@NoArgsConstructor
@Getter
public class MeetAuthorization extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorizationLevel authorization;



    @Builder
    public MeetAuthorization( Meet meet, User user, AuthorizationLevel authorization) {
        this.meet = meet;
        this.user = user;
        this.authorization = authorization;
    }

}