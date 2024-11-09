package com.sparta.mixin.domain.post.bookmark;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.post.bookmark.entity.CommunityBookmark;
import com.sparta.mixin.domain.post.meetpost.MeetPostService;
import com.sparta.mixin.domain.post.entity.MeetPost;
import com.sparta.mixin.domain.post.publicpost.PublicPostService;
import com.sparta.mixin.domain.post.entity.PublicPost;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MeetPostService meetPostService;
    private final PublicPostService publicPostService;
    private final AuthService authService;
    private final MeetService meetService;
    private final MeetAuthorizationService meetAuthorizationService;

    public void postPublicBookmark(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityBookmark communityBookmark = bookmarkRepository.findByPublicPostAndUser(publicPost,
            loginUser);
        if (communityBookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityBookmark newCommunityBookmark = new CommunityBookmark(publicPost, loginUser);
        bookmarkRepository.save(newCommunityBookmark);
    }

    public void deletePublicBookmark(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityBookmark publicBookmark = bookmarkRepository.findByPublicPostAndUser(publicPost,
            loginUser);
        if (publicBookmark == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        bookmarkRepository.delete(publicBookmark);
    }

    public void postMeetBookmark(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        Meet meet = meetService.findById(meetPost.getMeet().getId());
        if(meetAuthorizationService.findByMeetAndUser(meet,loginUser)==null){
            throw new CustomException(ErrorCode.INCORRECT_MEET_USER);
        }

        CommunityBookmark communityBookmark = bookmarkRepository.findByMeetPostAndUser(meetPost,
            loginUser);
        if (communityBookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityBookmark newCommunityBookmark = new CommunityBookmark(meetPost, loginUser);
        bookmarkRepository.save(newCommunityBookmark);
    }

    public void deleteMeetBookmark(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityBookmark meetBookmark = bookmarkRepository.findByMeetPostAndUser(meetPost,
            loginUser);
        if (meetBookmark == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        bookmarkRepository.delete(meetBookmark);
    }

    public CommunityBookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
