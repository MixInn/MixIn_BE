package com.sparta.mixin.domain.post;

import com.sparta.mixin.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository<T extends Post> extends JpaRepository<T,Long> {

    Page<T> findAllByUser_University(String university, Pageable pageable);
}
