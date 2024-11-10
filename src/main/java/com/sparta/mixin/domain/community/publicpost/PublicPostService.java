package com.sparta.mixin.domain.community.publicpost;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.community.publicpost.dto.PublicPostRequestDto;
import com.sparta.mixin.domain.community.publicpost.dto.PublicPostResponseDto;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
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
    private final UserService userService;

    public PublicPostResponseDto createPublicPost(PublicPostRequestDto publicPostRequestDto, List<String> fileUrls,
        User user) {
        User loginUser = userService.findByUsername(user.getUsername());
        PublicPost publicPost = new PublicPost(publicPostRequestDto,loginUser);
        publicPostRepository.save(publicPost);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl,publicPost);
            imageRepository.save(image);
        }
        return new PublicPostResponseDto(publicPost);
    }

    public Page<PublicPostResponseDto> getAllPublicPost(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Direction.DESC,"createdAt"));
        User loginUser = userService.findByUsername(user.getUsername());

        Page<PublicPost> responsePage = publicPostRepository.findAllByUser_University(loginUser.getUniversity(),pageable);

        return responsePage.map(PublicPostResponseDto::new);
    }

    public PublicPostResponseDto getPublicPost(Long postId, User user) {
        userService.findByUsername(user.getUsername());
        PublicPost publicPost = findById(postId);
        return new PublicPostResponseDto(publicPost);
    }

    public PublicPostResponseDto editPublicPost(Long postId,
        PublicPostRequestDto publicPostRequestDto, List<String> fileUrls, User user) {
        PublicPost publicPost = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if(publicPost.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        publicPost.updatePost(publicPostRequestDto);
        publicPostRepository.save(publicPost);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl,publicPost);
            imageRepository.save(image);
        }

        return new PublicPostResponseDto(publicPost);
    }

    public void deletePublicPost(Long postId, User user) {
        PublicPost publicPost = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if(publicPost.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        publicPostRepository.delete(publicPost);
    }

    public PublicPost findById(Long postId){
        return publicPostRepository.findById(postId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
