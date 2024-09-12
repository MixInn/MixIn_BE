package com.sparta.mixin.domain.image.entity;

import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.meetnotice.entity.MeetNotice;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "image")
@RequiredArgsConstructor
public class Image extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @ManyToOne
    @JoinColumn(name = "publicPost_id")
    private PublicPost publicPost;

    @ManyToOne
    @JoinColumn(name = "meetPost_id")
    private MeetPost meetPost;

    @ManyToOne
    @JoinColumn(name = "meetNotice_id")
    private MeetNotice meetNotice;

    @Builder
    public Image(String imageUrl,MeetPost meetPost){
        this.imageUrl=imageUrl;
        this.entityType=EntityType.MEETPOST;
        this.meetPost=meetPost;
    }
}
