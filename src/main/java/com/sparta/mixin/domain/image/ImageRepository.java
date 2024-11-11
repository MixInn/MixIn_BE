package com.sparta.mixin.domain.image;

import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByPost(Post post);

}
