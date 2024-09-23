package com.sparta.mixin.domain.community.bookmark;

import com.sparta.mixin.domain.community.bookmark.entity.Bookmark;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    Bookmark findByPublicPost(PublicPost publicPost);

    Bookmark findByMeetPost(MeetPost meetPost);
}
