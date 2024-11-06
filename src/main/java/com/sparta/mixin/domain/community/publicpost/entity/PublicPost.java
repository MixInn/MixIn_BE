package com.sparta.mixin.domain.community.publicpost.entity;

import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "PublicPost")
@RequiredArgsConstructor
public class PublicPost extends Post {

    public PublicPost(PostRequestDto postRequestDto, User user) {
        super(postRequestDto,user);
    }
    public void updatePost(PostRequestDto postRequestDto) {
        super.updatePost(postRequestDto);
    }
}
