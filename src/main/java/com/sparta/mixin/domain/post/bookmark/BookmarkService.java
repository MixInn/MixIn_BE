package com.sparta.mixin.domain.post.bookmark;

import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.bookmark.entity.PostBookmark;
import com.sparta.mixin.domain.post.entity.MeetNotice;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostService postService;
    private final UserService userService;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public void postBookmark(Long postId, User user) {
        Post post = postService.findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        if (post instanceof MeetPost) {
            Meet meet = meetService.findById(((MeetPost) post).getMeet().getId());
            if (meetAuthorizationService.findByMeetAndUser(meet, loginUser) == null) {
                throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
            }
        }

        if (post instanceof MeetNotice) {
            Meet meet = meetService.findById(((MeetNotice) post).getMeet().getId());
            if (meetAuthorizationService.findByMeetAndUser(meet, loginUser) == null) {
                throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
            }
        }

        PostBookmark postBookmark = bookmarkRepository.findByPostAndUser(post,
            loginUser);
        if (postBookmark != null) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_BOOKMARK);
        }
        PostBookmark newPostBookmark = new PostBookmark(post, loginUser);
        bookmarkRepository.save(newPostBookmark);
        post.increaseBookmarkCount();
        postService.save(post);
    }

    public void deleteBookmark(Long postId, User user) {
        Post post = postService.findById(postId);
        User loginUser = userService.findByUsername(user.getUsername());

        PostBookmark postBookmark = bookmarkRepository.findByPostAndUser(post,
            loginUser);
        if (postBookmark == null) {
            throw new CustomException(ErrorCode.NOT_EXISTING_BOOKMARK);
        }

        bookmarkRepository.delete(postBookmark);
        post.decreaseBookmarkCount();
        postService.save(post);
    }

    public PostBookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }

    public boolean existsByPostAndUser(Post post, User user){
        return bookmarkRepository.existsByPostAndUser(post,user);
    }
}
