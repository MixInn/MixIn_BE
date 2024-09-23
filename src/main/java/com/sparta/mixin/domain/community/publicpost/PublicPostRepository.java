package com.sparta.mixin.domain.community.publicpost;

import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicPostRepository extends JpaRepository<PublicPost,Long> {

}
