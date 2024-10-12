package com.sparta.mixin.domain.meetactivity.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MeetActivity")
@NoArgsConstructor
@Getter
public class MeetActivity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    private String activityPosition;
    private LocalDateTime date;
    private String content;


    @Builder
    public MeetActivity( Meet meet, String activityPosition, LocalDateTime date, String content) {
        this.meet = meet;
        this.activityPosition = activityPosition;
        this.date = date;
        this.content = content;
    }

    public void updateActivity(MeetActivityRequestDto meetActivityRequestDto){
        if (meetActivityRequestDto.getActivityPosition() != null) {
            this.activityPosition = meetActivityRequestDto.getActivityPosition();
        }

        if (meetActivityRequestDto.getDate() != null) {
            this.date = meetActivityRequestDto.getDate();
        }

        if (meetActivityRequestDto.getContent() != null) {
            this.content = meetActivityRequestDto.getContent();
        }
    }
}

