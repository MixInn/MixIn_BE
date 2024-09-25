package com.sparta.mixin.domain.community.comment;

import com.sparta.mixin.domain.community.comment.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommunityComment,Long> {

}
