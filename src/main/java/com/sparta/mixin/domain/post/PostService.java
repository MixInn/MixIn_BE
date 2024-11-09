package com.sparta.mixin.domain.post;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.dto.PostResponseDto;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.user.entity.User;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public abstract class PostService<T extends Post> {

    private final PostRepository<T> postRepository;
    private final ImageRepository imageRepository;
    private final AuthService authService;
    private final MeetService meetService;

    public PostResponseDto createPost(
        PostRequestDto postRequestDto, String postType, List<String> fileUrls,
        User user,Long meetId) {
        User loginUser = authService.findByUsername(user.getUsername());

        // MeetPost일 경우 meetId가 필수
        if (postType.equals("MEETPOST") && meetId == null) {
            throw new CustomException(ErrorCode.MISSING_MEET_ID);
        }

        // meetId가 존재하면 Meet 객체 찾기
        Meet meet = null;
        if (meetId != null) {
            meet = meetService.findById(meetId);
        }

        // 후크 메서드: 자식 클래스에서 처리해야 할 로직
        if (postType.equals("MEETPOST")) {
            checkMeetAuthorization(meet, loginUser);  // 자식 클래스에서 구현
        }

        T post = (T) PostFactory.createPost(postRequestDto,loginUser,postType,meet);
        postRepository.save(post);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl, post);
            imageRepository.save(image);
        }
        return new PostResponseDto(post);
    }

    public PostResponseDto editPost(Long postId, PostRequestDto postRequestDto, List<String> fileUrls, User user) {
        T post = findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(post.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        post.updatePost(postRequestDto);
        postRepository.save(post);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl,post);
            imageRepository.save(image);
        }
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        T post = findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(post.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        postRepository.delete(post);
    }

    public PostResponseDto getPost(Long postId, User user) {
        T post = findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(post instanceof MeetPost){
            Meet meet = meetService.findById(((MeetPost) post).getMeet().getId());
            checkMeetAuthorization(meet,loginUser);
        }

        return new PostResponseDto(post);
    }

    public Page<PostResponseDto> getAllPost(int page, int size,
        String postType, User user, Long meetId) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Direction.DESC,"createdAt"));
        User loginUser = authService.findByUsername(user.getUsername());

        // MeetPost일 경우 meetId가 필수
        if (postType.equals("MEETPOST") && meetId == null) {
            throw new CustomException(ErrorCode.MISSING_MEET_ID);
        }

        // meetId가 존재하면 Meet 객체 찾기
        Meet meet = null;
        if (meetId != null) {
            meet = meetService.findById(meetId);
        }

        // 후크 메서드: 자식 클래스에서 처리해야 할 로직
        if (postType.equals("MEETPOST")) {
            checkMeetAuthorization(meet, loginUser);  // 자식 클래스에서 구현
        }

        Page<T> responsePage = postRepository.findAllByUser_University(loginUser.getUniversity(),pageable);

        return responsePage.map(PostResponseDto::new);
    }

    // 후크 메서드를 오버라이드하여 MeetPost에만 필요한 로직 추가
    protected abstract void checkMeetAuthorization(Meet meet, User loginUser);

    public T findById(Long id) {
        return postRepository.findById(id).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
