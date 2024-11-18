package com.sparta.mixin.domain.meetannouncement.bookmark.service;

import com.sparta.mixin.domain.meetannouncement.bookmark.entity.AnnouncementBookmark;
import com.sparta.mixin.domain.meetannouncement.bookmark.entity.AnnouncementBookmarkRepository;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncementRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementBookmarkService {
    private final AnnouncementBookmarkRepository bookmarkRepository;
    private final MeetAnnouncementRepository meetAnnouncementRepository;

    @Transactional
    public void addBookmark(Long announcementId, User user) {
        MeetAnnouncement announcement = meetAnnouncementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEET_ANNOUNCEMENT_NOT_FOUND));

        if (bookmarkRepository.findByUserAndMeetAnnouncement(user, announcement).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_BOOKMARK);
        }

        AnnouncementBookmark bookmark = new AnnouncementBookmark(user, announcement);
        bookmarkRepository.save(bookmark);

        // bookmarkCount 증가
        announcement.incrementBookmarkCount();
        meetAnnouncementRepository.save(announcement);
    }

    @Transactional
    public void removeBookmark(Long announcementId, User user) {
        MeetAnnouncement announcement = meetAnnouncementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEET_ANNOUNCEMENT_NOT_FOUND));

        AnnouncementBookmark bookmark = bookmarkRepository.findByUserAndMeetAnnouncement(user, announcement)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTING_BOOKMARK));

        bookmarkRepository.delete(bookmark);

        // bookmarkCount 감소
        announcement.decrementBookmarkCount();
        meetAnnouncementRepository.save(announcement);
    }
}
