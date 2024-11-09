package com.sparta.mixin.domain.image;

import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.image.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findAllByPost(Post post);

}
