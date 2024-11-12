package com.sparta.mixin.domain.post.noticeread;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@RequiredArgsConstructor
@Table(name = "notice_read")
public class NoticeRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    private MeetNotice notice;

    public NoticeRead(User user, Meet meet, MeetNotice notice) {
        this.user = user;
        this.meet = meet;
        this.notice = notice;
    }
}
