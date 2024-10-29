package com.sparta.mixin.domain.community.bookmark;

import com.sparta.mixin.domain.auth.service.AuthService;
import com.sparta.mixin.domain.community.bookmark.entity.CommunityBookmark;
import com.sparta.mixin.domain.community.meetpost.MeetPostService;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.PublicPostService;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
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

    public void postPublicBookmark(Long postId, User user) {
        PublicPost publicPost = publicPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityBookmark communityBookmark = bookmarkRepository.findByPublicPostAndUser(publicPost,loginUser);
        if (communityBookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityBookmark newCommunityBookmark = new CommunityBookmark(publicPost,loginUser);
        bookmarkRepository.save(newCommunityBookmark);
    }

    public void deletePublicBookmark(Long bookmarkId, User user) {
        CommunityBookmark communityBookmark = findById(bookmarkId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(communityBookmark.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        bookmarkRepository.delete(communityBookmark);
    }

    public void postMeetBookmark(Long postId, User user) {
        MeetPost meetPost = meetPostService.findById(postId);
        User loginUser = authService.findByUsername(user.getUsername());

        CommunityBookmark communityBookmark = bookmarkRepository.findByMeetPostAndUser(meetPost,loginUser);
        if (communityBookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityBookmark newCommunityBookmark = new CommunityBookmark(meetPost,loginUser);
        bookmarkRepository.save(newCommunityBookmark);
    }

    public void deleteMeetBookmark(Long bookmarkId, User user) {
        CommunityBookmark communityBookmark = findById(bookmarkId);
        User loginUser = authService.findByUsername(user.getUsername());

        if(communityBookmark.getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        bookmarkRepository.delete(communityBookmark);
    }

    public CommunityBookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
