package com.sparta.mixin.domain.post.replycomment;

import com.sparta.mixin.domain.post.comment.entity.PostComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment,Long> {

    List<ReplyComment> findAllByPostComment(PostComment postComment);
}
