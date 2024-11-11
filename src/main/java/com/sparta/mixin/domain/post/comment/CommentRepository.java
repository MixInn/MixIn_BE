package com.sparta.mixin.domain.post.comment;

import com.sparta.mixin.domain.post.comment.entity.PostComment;
import com.sparta.mixin.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findAllByPost(Post post);
}
