package com.sparta.mixin.domain.post.noticeread;

import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeReadRepository extends JpaRepository<NoticeRead,Long> {

    NoticeRead findByUserAndNotice(User loginUser, MeetNotice post);
}
