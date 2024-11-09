package com.sparta.mixin.domain.post.bookmark;

import com.sparta.mixin.domain.post.bookmark.entity.CommunityBookmark;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<CommunityBookmark,Long> {

    CommunityBookmark findByPublicPostAndUser(PublicPost publicPost, User findUser);

    CommunityBookmark findByMeetPostAndUser(MeetPost meetPost, User findUser);
}
