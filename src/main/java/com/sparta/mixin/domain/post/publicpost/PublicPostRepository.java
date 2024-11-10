package com.sparta.mixin.domain.post.publicpost;

import com.sparta.mixin.domain.post.entity.PublicPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicPostRepository extends JpaRepository<PublicPost, Long> {

    Page<PublicPost> findAllByUser_University(String university, Pageable pageable);
}
