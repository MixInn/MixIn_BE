package com.sparta.mixin.domain.image;

import com.sparta.mixin.domain.image.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findAllByMeetPost(Long postId);
}
