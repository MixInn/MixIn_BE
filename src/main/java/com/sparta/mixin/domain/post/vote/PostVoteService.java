package com.sparta.mixin.domain.post.vote;

import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.post.vote.dto.VoteOptionResponseDto;
import com.sparta.mixin.domain.post.vote.dto.VoteRequestDto;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import com.sparta.mixin.domain.post.vote.entity.PostVote;
import com.sparta.mixin.domain.post.vote.entity.VoteOption;
import com.sparta.mixin.domain.post.vote.entity.VoteResult;
import com.sparta.mixin.domain.post.vote.repository.PostVoteRepository;
import com.sparta.mixin.domain.post.vote.repository.VoteOptionRepository;
import com.sparta.mixin.domain.post.vote.repository.VoteResultRepository;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostVoteService {

    private final PostVoteRepository postVoteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final PostService postService;
    private final UserService userService;
    private final VoteResultRepository voteResultRepository;

    public VoteResponseDto getPostVote(Long postId, User user) {
        Post post = postService.findById(postId);
        userService.findByUsername(user.getUsername());

        PostVote postVote = postVoteRepository.findByPost(post);

        if (postVote == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        List<String> optionTextList = voteOptionRepository.findAllByPostVote(postVote).stream()
            .map(VoteOption::getOptionText).toList();

        return new VoteResponseDto(postVote, optionTextList);
    }

    public VoteResponseDto editPostVote(Long voteId, VoteRequestDto voteRequestDto, User user) {
        PostVote postVote = findById(voteId);
        User loginUser = userService.findByUsername(user.getUsername());
        if (postVote.getPost().getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        postVote.updateVote(voteRequestDto);

        voteOptionRepository.deleteAllByPostVote(postVote);

        List<VoteOption> updatedVoteOptions = voteRequestDto.getVoteOption().stream()
            .map(optionText -> new VoteOption(postVote, optionText)).toList();
        voteOptionRepository.saveAll(updatedVoteOptions);

        List<String> optionTextList = updatedVoteOptions.stream().map(VoteOption::getOptionText)
            .toList();

        return new VoteResponseDto(postVote, optionTextList);
    }

    public void deletePostVote(Long voteId, User user) {
        PostVote postVote = findById(voteId);
        User loginUser = userService.findByUsername(user.getUsername());
        if (postVote.getPost().getUser() != loginUser) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        postVoteRepository.delete(postVote);
    }

    public void submitVote(PostVote postVote, List<Long> voteOptionIds, User user) {
        User loginUser = userService.findByUsername(user.getUsername());
        for (Long voteOptionId : voteOptionIds) {
            VoteOption voteOption = voteOptionRepository.findById(voteOptionId).orElseThrow(
                () -> new CustomException(ErrorCode.BAD_REQUEST)
            );
            VoteResult voteResult =
                postVote.isAnonymous() ? new VoteResult(voteOption, null, LocalDateTime.now())
                    : new VoteResult(voteOption, loginUser, LocalDateTime.now());
            voteResultRepository.save(voteResult);
        }
    }

    public List<VoteOptionResponseDto> getAllVoteOption(Long voteId, User user) {
        userService.findByUsername(user.getUsername());
        PostVote postVote = findById(voteId);
        List<VoteOptionResponseDto> responseDtos = new ArrayList<>();

        List<VoteOption> voteOptionList = voteOptionRepository.findAllByPostVote(postVote);
        for (VoteOption voteOption : voteOptionList) {
            Long voteCount = voteResultRepository.countByVoteOption(voteOption);
            List<String> voteUser = voteResultRepository.findUserByVoteOption(voteOption).stream().map(User::getName).toList();
            VoteOptionResponseDto responseDto = new VoteOptionResponseDto(voteOption,voteCount,voteUser);
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    public PostVote findById(Long voteId) {
        return postVoteRepository.findById(voteId).orElseThrow(
            () -> new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
