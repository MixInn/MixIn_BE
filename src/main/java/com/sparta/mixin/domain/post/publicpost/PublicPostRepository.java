package com.sparta.mixin.domain.post.publicpost;

import com.sparta.mixin.domain.post.entity.PublicPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublicPostRepository extends JpaRepository<PublicPost, Long> {

    Page<PublicPost> findAllByUser_UniversityAndPostType(String university, String postType, Pageable pageable);

    Page<PublicPost> findAllByUser_UniversityAndPostTypeOrderByClickCountDesc(String university, String postType, Pageable pageable);

    Page<PublicPost> findAllByUser_UniversityAndPostTypeOrderByLikeCountDesc(String university, String postType, Pageable pageable);

    @Query("SELECT p FROM PublicPost p WHERE p.user.university=:university AND p.postType=:postType AND (p.title Like %:searchWord% OR p.content LIKE %:searchWord%)")
    Page<PublicPost> findAllByUser_UniversityAndPostTypeAndTitleContainingOrContentContaining(String university,
        String postType, String searchWord, Pageable pageable);
}
