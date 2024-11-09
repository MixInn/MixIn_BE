package com.sparta.mixin.domain.post.bookmark;

import com.sparta.mixin.domain.post.bookmark.entity.PostBookmark;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<PostBookmark,Long> {

    PostBookmark findByPublicPostAndUser(PublicPost publicPost, User findUser);

    PostBookmark findByMeetPostAndUser(MeetPost meetPost, User findUser);
}
