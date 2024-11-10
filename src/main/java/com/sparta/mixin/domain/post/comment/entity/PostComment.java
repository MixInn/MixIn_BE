package com.sparta.mixin.domain.post.comment.entity;

import com.sparta.mixin.domain.community.comment.dto.CommentRequestDto;
import com.sparta.mixin.domain.post.PostType;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "community_comment")
@RequiredArgsConstructor
public class PostComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "publicPost_id")
    private PublicPost publicPost;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "meetPost_id")
    private MeetPost meetPost;

    @Enumerated(EnumType.STRING)
    private PostType communityType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String comment;


    public PostComment(PublicPost publicPost, CommentRequestDto commentRequestDto,
        User loginUser) {
        this.comment = commentRequestDto.getComment();
        this.publicPost = publicPost;
        this.communityType = PostType.PUBLICPOST;
        this.user = loginUser;
    }

    public PostComment(MeetPost meetPost, CommentRequestDto commentRequestDto,
        User loginUser) {
        this.comment = commentRequestDto.getComment();
        this.meetPost = meetPost;
        this.communityType = PostType.MEETPOST;
        this.user = loginUser;
    }
}
