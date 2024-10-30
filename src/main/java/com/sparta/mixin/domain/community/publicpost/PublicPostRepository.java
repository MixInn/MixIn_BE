package com.sparta.mixin.domain.community.publicpost;

import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicPostRepository extends JpaRepository<PublicPost,Long> {

    Page<PublicPost> findAllByUser_UniversityAndUser_Major(String university, String major, Pageable pageable);
}
