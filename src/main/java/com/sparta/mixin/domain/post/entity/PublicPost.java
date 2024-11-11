package com.sparta.mixin.domain.post.entity;

import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "PublicPost")
@RequiredArgsConstructor
@DiscriminatorValue("PUBLICPOST")
public class PublicPost extends Post {

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "bookmark_count")
    private Long bookmarkCount;

    @Column(name = "comment_count")
    private Long commentCount;

    public PublicPost(PostRequestDto postRequestDto, User user) {
        super(postRequestDto, user);
    }

    public void updatePost(PostRequestDto postRequestDto) {
        super.updatePost(postRequestDto);
    }

    @Override
    public void increaseBookmarkCount() {
        this.bookmarkCount++;
    }

    @Override
    public void decreaseBookmarkCount() {
        this.bookmarkCount--;
    }

    @Override
    public void increaseLikeCount() {
        this.likeCount++;
    }

    @Override
    public void decreaseLikeCount() {
        this.likeCount--;
    }

    @Override
    public void increaseCommentCount() {
        this.commentCount++;
    }

    @Override
    public void decreaseCommentCount() {
        this.commentCount--;
    }

    @Override
    public void markAsRead() {

    }
}
