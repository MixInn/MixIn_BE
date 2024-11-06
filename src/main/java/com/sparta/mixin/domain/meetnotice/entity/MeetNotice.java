package com.sparta.mixin.domain.meetnotice.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "meetNotice")
@RequiredArgsConstructor
public class MeetNotice extends Post {
    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    @Builder
    public MeetNotice(PostRequestDto postRequestDto, User user,Meet meet){
        super(postRequestDto,user);
        this.meet=meet;
    }

    public void updatePost(PostRequestDto postRequestDto) {
        super.updatePost(postRequestDto);
    }
}

