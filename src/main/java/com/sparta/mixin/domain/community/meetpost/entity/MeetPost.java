package com.sparta.mixin.domain.community.meetpost.entity;

import com.sparta.mixin.domain.community.meetpost.dto.MeetPostRequestDto;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "MeetPost")
@RequiredArgsConstructor
public class MeetPost extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public MeetPost(MeetPostRequestDto meetPostRequestDto,Meet meet,User user){
        this.meet=meet;
        this.title= meetPostRequestDto.getTitle();
        this.content= meetPostRequestDto.getContent();
        this.user=user;
    }

    public void updatePost(MeetPostRequestDto meetPostRequestDto) {
        this.title= meetPostRequestDto.getTitle();
        this.content= meetPostRequestDto.getContent();
    }
}
