package com.sparta.mixin.domain.community.publicpost;

import com.sparta.mixin.domain.community.publicpost.dto.PublicPostRequestDto;
import com.sparta.mixin.domain.community.publicpost.dto.PublicPostResponseDto;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    public Page<PublicPostResponseDto> getAllPublicPost(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Direction.DESC,"createdAt"));

        Page<PublicPost> responsePage = publicPostRepository.findAll(pageable);

        return responsePage.map(PublicPostResponseDto::new);
    }

    public PublicPostResponseDto getPublicPost(Long postId) {
        PublicPost publicPost = findById(postId);
        return new PublicPostResponseDto(publicPost);
    }

    public PublicPostResponseDto editPublicPost(Long postId,
        PublicPostRequestDto publicPostRequestDto) {
        PublicPost publicPost = findById(postId);
        publicPost.updatePost(publicPostRequestDto);
        publicPostRepository.save(publicPost);

        return new PublicPostResponseDto(publicPost);
    }

    public void deletePublicPost(Long postId) {
        PublicPost publicPost = findById(postId);
        publicPostRepository.delete(publicPost);
    }

    public PublicPost findById(Long postId){
        return publicPostRepository.findById(postId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
