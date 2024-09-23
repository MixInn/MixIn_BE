package com.sparta.mixin.domain.community.publicpost;

import com.sparta.mixin.domain.community.publicpost.dto.PublicPostRequestDto;
import com.sparta.mixin.domain.community.publicpost.dto.PublicPostResponseDto;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.entity.Image;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicPostService {
    private final PublicPostRepository publicPostRepository;
    private final ImageRepository imageRepository;

    public PublicPostResponseDto createPublicPost(PublicPostRequestDto publicPostRequestDto, List<String> fileUrls) {
        PublicPost publicPost = new PublicPost(publicPostRequestDto);
        publicPostRepository.save(publicPost);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl,publicPost);
            imageRepository.save(image);
        }
        return new PublicPostResponseDto(publicPost);
    }
}
