package com.sparta.mixin.domain.community.comment;

import com.sparta.mixin.domain.community.comment.entity.CommunityComment;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommunityComment,Long> {

    List<CommunityComment> findAllByPublicPost(PublicPost publicPost);

    List<CommunityComment> findAllByMeetPost(MeetPost meetPost);
}
