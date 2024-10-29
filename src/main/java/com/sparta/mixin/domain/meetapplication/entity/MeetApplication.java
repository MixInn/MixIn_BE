package com.sparta.mixin.domain.meetapplication.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetApplication")
@NoArgsConstructor
@Getter
public class MeetApplication extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;
    @Enumerated(EnumType.STRING)
    private ResultEnum resultEnum;

    private String reason;


    // Getters and setters
    @Builder
    public MeetApplication(Meet meet, User user, String content) {
        this.meet = meet;
        this.user = user;
        this.content = content;
        this.resultEnum = ResultEnum.PENDING;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void acceptApplication(){
        this.resultEnum = ResultEnum.ACCEPTED;
    }
    public void rejectApplication(String reason){
        this.reason = reason;
        this.resultEnum = ResultEnum.REJECTED;
    }
}
