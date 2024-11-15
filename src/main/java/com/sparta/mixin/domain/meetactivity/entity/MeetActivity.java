package com.sparta.mixin.domain.meetactivity.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meetactivity.dto.MeetActivityRequestDto;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private String title;
    private String position;
    private LocalDate date;
    private String content;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityParticipation> participants = new ArrayList<>();


    @Builder
    public MeetActivity(Meet meet, String title, String position, LocalDate date, String content) {
        this.meet = meet;
        this.title = title;
        this.position = position;
        this.date = date;
        this.content = content;
    }

    public void updateActivity(MeetActivityRequestDto meetActivityRequestDto){
        if (meetActivityRequestDto.getTitle() != null){
            this.title = meetActivityRequestDto.getTitle();
        }

        if (meetActivityRequestDto.getPosition() != null) {
            this.position = meetActivityRequestDto.getPosition();
        }

        if (meetActivityRequestDto.getDate() != null) {
            this.date = meetActivityRequestDto.getDate();
        }

        if (meetActivityRequestDto.getContent() != null) {
            this.content = meetActivityRequestDto.getContent();
        }
    }
}

