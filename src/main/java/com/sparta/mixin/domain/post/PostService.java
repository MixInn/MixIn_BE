package com.sparta.mixin.domain.post;

import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.dto.MeetNoticeResponseDto;
import com.sparta.mixin.domain.post.dto.MeetPostResponseDto;
import com.sparta.mixin.domain.post.dto.PostRequestDto;
import com.sparta.mixin.domain.post.dto.PostResponseDto;
import com.sparta.mixin.domain.post.dto.PublicPostResponseDto;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.post.meetnotice.MeetNoticeRepository;
import com.sparta.mixin.domain.post.meetpost.MeetPostRepository;
import com.sparta.mixin.domain.post.noticeread.NoticeRead;
import com.sparta.mixin.domain.post.noticeread.NoticeReadRepository;
import com.sparta.mixin.domain.post.publicpost.PublicPostRepository;
import com.sparta.mixin.domain.post.vote.PostVote;
import com.sparta.mixin.domain.post.vote.PostVoteRepository;
import com.sparta.mixin.domain.post.vote.VoteOption;
import com.sparta.mixin.domain.post.vote.VoteOptionRepository;
import com.sparta.mixin.domain.post.vote.VoteRequestDto;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public abstract class PostService<T extends Post> {

    private final PostRepository<T> postRepository;
    private final MeetPostRepository meetPostRepository;
    private final MeetNoticeRepository meetNoticeRepository;
    private final PublicPostRepository publicPostRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final MeetService meetService;
    private final NoticeReadRepository noticeReadRepository;
    private final PostVoteRepository postVoteRepository;
    private final VoteOptionRepository voteOptionRepository;

    public PostResponseDto createPost(
        PostRequestDto postRequestDto, String postType, List<String> fileUrls,
        User user, Long meetId) {
        User loginUser = userService.findByUsername(user.getUsername());

        // MeetPost이거나 MeetNotice인 경우 meetId가 필수
        if ((postType.equals("MEETPOST") || postType.equals("MEETNOTICE")) && meetId == null) {
            throw new CustomException(ErrorCode.MISSING_MEET_ID);
        }

        // meetId가 존재하면 Meet 객체 찾기
        Meet meet = null;
        if (meetId != null) {
            meet = meetService.findById(meetId);
        }

        // 후크 메서드: 자식 클래스에서 처리해야 할 로직
        if (postType.equals("MEETPOST") || postType.equals("MEETNOTICE")) {
            checkMeetAuthorization(meet, loginUser);  // 자식 클래스에서 구현
        }

        T post = (T) PostFactory.createPost(postRequestDto, loginUser, postType, meet);
        save(post);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl, post);
            imageRepository.save(image);
        }

        if(postRequestDto.getVoteRequestDto()!=null){
            PostVote vote = new PostVote(postRequestDto.getVoteRequestDto(),post);
            postVoteRepository.save(vote);
            for (String optionText : postRequestDto.getVoteRequestDto().getVoteOption()) {
                VoteOption voteOption = new VoteOption(vote,optionText);
                voteOptionRepository.save(voteOption);
            }
        }

        if (post instanceof MeetPost) {
            return new MeetPostResponseDto((MeetPost) post);
        } else if (post instanceof MeetNotice) {
            boolean isRead = noticeReadRepository.findByUserAndNotice(loginUser, (MeetNotice) post) != null;
            return new MeetNoticeResponseDto((MeetNotice) post, isRead);
        } else {
            return new PublicPostResponseDto((PublicPost) post);
        }
    }

    public PostResponseDto editPost(Long postId, PostRequestDto postRequestDto,
        List<String> fileUrls, User user) {
        T post = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (post.getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        post.updatePost(postRequestDto);
        save(post);

        for (String fileUrl : fileUrls) {
            Image image = new Image(fileUrl, post);
            imageRepository.save(image);
        }

        if (post instanceof MeetPost) {
            return new MeetPostResponseDto((MeetPost) post);
        } else if (post instanceof MeetNotice) {
            boolean isRead = noticeReadRepository.findByUserAndNotice(loginUser, (MeetNotice) post) != null;
            return new MeetNoticeResponseDto((MeetNotice) post, isRead);
        } else {
            return new PublicPostResponseDto((PublicPost) post);
        }
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        T post = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (post.getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        postRepository.delete(post);
    }

    public PostResponseDto getPost(Long postId, User user) {
        T post = findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (post instanceof MeetPost) {
            Meet meet = meetService.findById(((MeetPost) post).getMeet().getId());
            checkMeetAuthorization(meet, loginUser);
            post.increaseClickCount();
            save(post);
            return new MeetPostResponseDto((MeetPost) post);
        }

        if (post instanceof MeetNotice) {
            Meet meet = meetService.findById(((MeetNotice) post).getMeet().getId());
            checkMeetAuthorization(meet, loginUser);
            NoticeRead noticeRead =noticeReadRepository.findByUserAndNotice(loginUser,(MeetNotice) post);
            if(noticeRead==null){
                NoticeRead newNoticeRead = new NoticeRead(loginUser,meet,(MeetNotice) post);
                noticeReadRepository.save(newNoticeRead);
            }
            post.increaseClickCount();
            save(post);
            return new MeetNoticeResponseDto((MeetNotice) post,true);
        }

        if(post instanceof PublicPost){
            post.increaseClickCount();
            save(post);
        }

        return new PublicPostResponseDto((PublicPost) post);
    }

    public Page<? extends PostResponseDto> getAllPost(int page, int size, String orderBy, String searchWord, String postType, User user, Long meetId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        User loginUser = userService.findByUsername(user.getUsername());

        if ((postType.equals("MEETPOST") || postType.equals("MEETNOTICE")) && meetId == null) {
            throw new CustomException(ErrorCode.MISSING_MEET_ID);
        }

        Meet meet = null;
        if (meetId != null) {
            meet = meetService.findById(meetId);
        }

        if(!searchWord.isEmpty()){
            if(postType.equals("MEETPOST")){
                checkMeetAuthorization(meet, loginUser);
                return meetPostRepository.findAllByUser_UniversityAndPostTypeAndMeetAndTitleContainingOrContentContaining(loginUser.getUniversity(),postType,meet,searchWord,pageable).map(MeetPostResponseDto::new);
            }
            if(postType.equals("PUBLICPOST")){
                return publicPostRepository.findAllByUser_UniversityAndPostTypeAndTitleContainingOrContentContaining(loginUser.getUniversity(),postType,searchWord,pageable).map(PublicPostResponseDto::new);
            }
        }

        if (postType.equals("MEETPOST")) {
            checkMeetAuthorization(meet, loginUser);
            if(orderBy.equals("click")){
                return meetPostRepository.findAllByUser_UniversityAndPostTypeAndMeetOrderByClickCountDesc(
                    loginUser.getUniversity(), postType, meet, pageable
                ).map(MeetPostResponseDto::new);
            }
            if(orderBy.equals("like")){
                return meetPostRepository.findAllByUser_UniversityAndPostTypeAndMeetOrderByLikeCountDesc(
                    loginUser.getUniversity(), postType, meet, pageable
                ).map(MeetPostResponseDto::new);
            }
            return meetPostRepository.findAllByUser_UniversityAndPostTypeAndMeet(
                loginUser.getUniversity(), postType, meet, pageable
            ).map(MeetPostResponseDto::new);
        }

        if (postType.equals("MEETNOTICE")) {
            checkMeetAuthorization(meet, loginUser);
            Page<MeetNotice> noticePage = meetNoticeRepository.findAllByUser_UniversityAndPostTypeAndMeet(
                loginUser.getUniversity(), postType, meet, pageable
            );

            return noticePage.map(notice -> {
                boolean isRead = noticeReadRepository.findByUserAndNotice(loginUser, notice) != null;
                return new MeetNoticeResponseDto(notice, isRead);
            });
        }

        if(postType.equals("PUBLICPOST")){
            if(orderBy.equals("click")){
                return publicPostRepository.findAllByUser_UniversityAndPostTypeOrderByClickCountDesc(loginUser.getUniversity(), postType, pageable)
                    .map(PublicPostResponseDto::new);
            }
            if(orderBy.equals("like")){
                return publicPostRepository.findAllByUser_UniversityAndPostTypeOrderByLikeCountDesc(loginUser.getUniversity(), postType, pageable)
                    .map(PublicPostResponseDto::new);
            }
        }

        return publicPostRepository.findAllByUser_UniversityAndPostType(loginUser.getUniversity(), postType, pageable)
            .map(PublicPostResponseDto::new);
    }

    // 후크 메서드를 오버라이드하여 MeetPost에만 필요한 로직 추가
    protected abstract void checkMeetAuthorization(Meet meet, User loginUser);

    public T findById(Long id) {
        return postRepository.findById(id).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }

    public void save(T post){
        postRepository.save(post);
    }
}
