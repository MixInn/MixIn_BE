package com.sparta.mixin.domain.post.entity;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.Column;
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

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "bookmark_count")
    private Long bookmarkCount = 0L;

    @Column(name = "comment_count")
    private Long commentCount = 0L;

    @Column(name = "read_count")
    private Long readCount = 0L;

    @Builder
    public MeetPost(PostRequestDto postRequestDto, User user, Meet meet) {
        super(postRequestDto, user);
        this.meet = meet;
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
    public void increaseReadCount() {
        this.readCount++;
    }
}
