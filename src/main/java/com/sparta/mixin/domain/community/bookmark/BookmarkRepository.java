package com.sparta.mixin.domain.community.bookmark;

import com.sparta.mixin.domain.community.bookmark.entity.CommunityBookmark;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<CommunityBookmark,Long> {

    CommunityBookmark findByPublicPost(PublicPost publicPost);

    CommunityBookmark findByMeetPost(MeetPost meetPost);
}
