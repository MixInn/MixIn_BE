package com.sparta.mixin.domain.community.meetpost;

import com.sparta.mixin.domain.community.meetpost.dto.MeetPostRequestDto;
import com.sparta.mixin.domain.community.meetpost.dto.MeetPostResponseDto;
import com.sparta.mixin.domain.community.meetpost.entity.MeetPost;
import com.sparta.mixin.domain.image.ImageRepository;
import com.sparta.mixin.domain.image.dto.ImageResponseDto;
import com.sparta.mixin.domain.image.entity.Image;
import com.sparta.mixin.domain.meet.MeetService;
import com.sparta.mixin.domain.meet.entity.Meet;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.util.ArrayList;
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
public class MeetPostService {
    private final MeetPostRepository meetPostRepository;
    private final MeetService meetService;
    private final ImageRepository imageRepository;

    public MeetPostResponseDto createMeetPost(Long meetId, MeetPostRequestDto meetPostRequestDto,
        List<String> fileUrls) {
        Meet meet = meetService.findById(meetId);

        MeetPost meetPost = MeetPost.builder()
            .meetPostRequestDto(meetPostRequestDto)
            .meet(meet)
            .build();

        meetPostRepository.save(meetPost);

        for (String fileUrl : fileUrls) {
            Image image = Image.builder()
                .imageUrl(fileUrl)
                .meetPost(meetPost)
                .build();
            imageRepository.save(image);
        }

        return new MeetPostResponseDto(meetPost);
    }

    public MeetPostResponseDto getMeetPost(Long postId) {

        MeetPost meetPost = findById(postId);

        MeetPostResponseDto meetPostResponseDto = new MeetPostResponseDto(meetPost);

        return meetPostResponseDto;
    }

    public MeetPostResponseDto editMeetPost(Long postId, MeetPostRequestDto meetPostRequestDto) {
        MeetPost meetPost = findById(postId);
        meetPost.updatePost(meetPostRequestDto);
        meetPostRepository.save(meetPost);

        return new MeetPostResponseDto(meetPost);
    }

    @Transactional
    public void deleteMeetPost(Long postId) {
        MeetPost meetPost = findById(postId);
        meetPostRepository.delete(meetPost);
    }

    public Page<MeetPostResponseDto> getAllMeetPost(Long meetId,int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Direction.DESC,"createdAt"));
        Meet meet = meetService.findById(meetId);

        Page<MeetPost> responseDtos = meetPostRepository.findAllByMeet(meet,pageable);

        return responseDtos.map(MeetPostResponseDto::new);
    }

    public List<ImageResponseDto> getAllPostImages(Long postId) {
        MeetPost meetPost = findById(postId);
        List<Image> imageList = imageRepository.findAllByMeetPost(meetPost.getId());
        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();
        for (Image image : imageList) {
            ImageResponseDto imageResponseDto = new ImageResponseDto(image);
            imageResponseDtos.add(imageResponseDto);
        }
        return imageResponseDtos;
    }

    public MeetPost findById(Long postId){
        return meetPostRepository.findById(postId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
