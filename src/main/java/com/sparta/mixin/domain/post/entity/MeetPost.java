package com.sparta.mixin.domain.post.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "MeetPost")
@RequiredArgsConstructor
@DiscriminatorValue("MEETPOST")
public class MeetPost extends Post {

    @ManyToOne
    @JoinColumn(name = "meet_id", nullable = false)
    private Meet meet;

    @Builder
    public MeetPost(PostRequestDto postRequestDto,User user,Meet meet){
        super(postRequestDto,user);
        this.meet=meet;
    }

    public void updatePost(PostRequestDto postRequestDto) {
        super.updatePost(postRequestDto);
    }
}
