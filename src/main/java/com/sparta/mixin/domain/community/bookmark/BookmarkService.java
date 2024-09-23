package com.sparta.mixin.domain.community.bookmark;

import com.sparta.mixin.domain.community.bookmark.entity.Bookmark;
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
        Bookmark bookmark = bookmarkRepository.findByPublicPost(publicPost);
        if (bookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        Bookmark newBookmark = new Bookmark(publicPost);
        bookmarkRepository.save(newBookmark);
    }

    public void deletePublicBookmark(Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId);
        bookmarkRepository.delete(bookmark);
    }

    public void postMeetBookmark(Long postId) {
        MeetPost meetPost = meetPostService.findById(postId);
        Bookmark bookmark = bookmarkRepository.findByMeetPost(meetPost);
        if (bookmark != null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        Bookmark newBookmark = new Bookmark(meetPost);
        bookmarkRepository.save(newBookmark);
    }

    public void deleteMeetBookmark(Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId);
        bookmarkRepository.delete(bookmark);
    }

    public Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
