package com.sparta.mixin.domain.meetannouncement.bookmark.entity;

import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class AnnouncementBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "announcement_id", nullable = false)
    private MeetAnnouncement meetAnnouncement;

    @Builder
    public AnnouncementBookmark(User user, MeetAnnouncement meetAnnouncement) {
        this.user = user;
        this.meetAnnouncement = meetAnnouncement;
    }

}
