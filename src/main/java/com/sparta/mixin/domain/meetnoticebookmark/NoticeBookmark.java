package com.sparta.mixin.domain.meetnoticebookmark;

import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "noticeBookmark")
public class NoticeBookmark extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "meetNotice_id", nullable = false)
    private MeetNotice meetNotice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and setters
}
