package com.sparta.mixin.domain.community.bookmark;

import com.sparta.mixin.domain.community.bookmark.entity.CommunityBookmark;
import com.sparta.mixin.domain.community.meetpost.MeetPostService;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.community.publicpost.PublicPostService;
import com.sparta.mixin.domain.community.publicpost.entity.PublicPost;
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

    public void postPublicBookmark(Long postId) {
        PublicPost publicPost = publicPostService.findById(postId);
        CommunityBookmark communityBookmark = bookmarkRepository.findByPublicPost(publicPost);
        if (communityBookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityBookmark newCommunityBookmark = new CommunityBookmark(publicPost);
        bookmarkRepository.save(newCommunityBookmark);
    }

    public void deletePublicBookmark(Long bookmarkId) {
        CommunityBookmark communityBookmark = findById(bookmarkId);
        bookmarkRepository.delete(communityBookmark);
    }

    public void postMeetBookmark(Long postId) {
        MeetPost meetPost = meetPostService.findById(postId);
        CommunityBookmark communityBookmark = bookmarkRepository.findByMeetPost(meetPost);
        if (communityBookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        CommunityBookmark newCommunityBookmark = new CommunityBookmark(meetPost);
        bookmarkRepository.save(newCommunityBookmark);
    }

    public void deleteMeetBookmark(Long bookmarkId) {
        CommunityBookmark communityBookmark = findById(bookmarkId);
        bookmarkRepository.delete(communityBookmark);
    }

    public CommunityBookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
