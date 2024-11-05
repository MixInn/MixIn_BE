package com.sparta.mixin.domain.meetapplication.service;

import com.sparta.mixin.domain.meet.entity.AuthorizationLevel;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.domain.meet.entity.MeetAuthorization;
import com.sparta.mixin.domain.meet.service.MeetAuthorizationService;
import com.sparta.mixin.domain.meet.service.MeetService;
import com.sparta.mixin.domain.meetannouncement.entity.MeetAnnouncement;
import com.sparta.mixin.domain.meetannouncement.service.MeetAnnouncementService;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationRequestDto;
import com.sparta.mixin.domain.meetapplication.dto.MeetApplicationResponseDto;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplication;
import com.sparta.mixin.domain.meetapplication.entity.MeetApplicationRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetApplicationService {
    private final MeetApplicationRepository meetApplicationRepository;
    private final MeetAuthorizationService meetAuthorizationService;
    private final MeetAnnouncementService meetAnnouncementService;

    public void createMeetApplication(Long meetAnnouncementId,MeetApplicationRequestDto requestDto,User user) {
        // 모임 지원서
        MeetAnnouncement meetAnnouncement = meetAnnouncementService.findById(meetAnnouncementId);
        Meet meet = meetAnnouncement.getMeet();

        boolean isMember = meetAuthorizationService.isUserMemberOfMeet(meet, user);
        if (isMember) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        MeetApplication meetApplication = MeetApplication.builder()
                .meetAnnouncement(meetAnnouncement)
                .user(user)
                .content(requestDto.getContent())
                .build();

        meetApplicationRepository.save(meetApplication);
    }

    public void updateMeetApplication(Long meetApplicationId, MeetApplicationRequestDto requestDto,User user) {
        MeetApplication meetApplication = findById(meetApplicationId);

        if (!meetApplication.getUser().equals(user)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meetApplication.updateContent(requestDto.getContent());

        meetApplicationRepository.save(meetApplication);
    }


    public void deleteMeetApplication(Long meetApplicationId,User user) {

        MeetApplication meetApplication = findById(meetApplicationId);

        if (!meetApplication.getUser().equals(user)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        meetApplicationRepository.delete(meetApplication);
    }

    public void acceptMeetApplication(Long meetApplicationId) {
        MeetApplication meetApplication = findById(meetApplicationId);

        // 수락 상태로 변경
        meetApplication.acceptApplication();

        // 신청이 수락되면 MeetAuthorization에 추가
        Meet meet = meetApplication.getMeetAnnouncement().getMeet();
        User user = meetApplication.getUser();

        // 기본 권한을 MEMBER로 설정 (필요에 따라 LEADER 등으로 설정할 수 있음)
        MeetAuthorization meetAuthorization = MeetAuthorization.builder()
                .meet(meet)
                .user(user)
                .authorization(AuthorizationLevel.MEMBER)  // 기본적으로 MEMBER 권한 부여
                .build();

        // MeetAuthorization 저장
        meetAuthorizationService.save(meetAuthorization);


        // 업데이트된 상태 저장
        meetApplicationRepository.save(meetApplication);
    }

    public void rejectMeetApplication(Long meetApplicationId,String reason) {
        MeetApplication meetApplication = findById(meetApplicationId);

        // 거절 상태로 변경
        meetApplication.rejectApplication(reason);

        // 업데이트된 상태 저장
        meetApplicationRepository.save(meetApplication);
    }

    public List<MeetApplicationResponseDto> getMyApplications(User user) {
        List<MeetApplication> applications = meetApplicationRepository.findByUser(user);
        return applications.stream()
                .map(MeetApplicationResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<MeetApplicationResponseDto> getApplicationsForMeet(Long meetId, User user) {
        MeetAnnouncement meetAnnouncement = meetAnnouncementService.findByMeetId(meetId);
        Meet meet = meetAnnouncement.getMeet();

        AuthorizationLevel role = meetAuthorizationService.getUserRole(meet, user);

        if (role != AuthorizationLevel.LEADER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<MeetApplication> applications = meetApplicationRepository.findByMeetAnnouncement(meetAnnouncement);
        return applications.stream()
                .map(MeetApplicationResponseDto::new)
                .collect(Collectors.toList());
    }

    public MeetApplication findById(Long meetApplicationId) {
        return meetApplicationRepository.findById(meetApplicationId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
