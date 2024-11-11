package com.sparta.mixin.domain.post.dto;

import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.entity.PublicPost;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long meetId;
    private Long bookmarkCount;
    private Long likeCount;
    private Long commentCount;
    private boolean isRead;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        if (post instanceof MeetPost) {
            this.meetId = ((MeetPost) post).getMeet().getId();
            this.bookmarkCount=((MeetPost) post).getBookmarkCount();
            this.likeCount=((MeetPost) post).getLikeCount();
            this.commentCount=((MeetPost) post).getCommentCount();
        } else if (post instanceof MeetNotice) {
            this.meetId = ((MeetNotice) post).getMeet().getId();
            this.isRead=((MeetNotice) post).isRead();
        } else if (post instanceof PublicPost) {
            this.bookmarkCount=((PublicPost) post).getBookmarkCount();
            this.likeCount=((PublicPost) post).getLikeCount();
            this.commentCount=((PublicPost) post).getCommentCount();
        }
    }

}
