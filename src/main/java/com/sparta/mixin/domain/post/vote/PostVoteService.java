package com.sparta.mixin.domain.post.vote;

import com.sparta.mixin.domain.post.PostService;
import com.sparta.mixin.domain.post.entity.Post;
import com.sparta.mixin.domain.user.entity.User;
import com.sparta.mixin.domain.user.service.UserService;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
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

    public VoteResponseDto getPostVote(Long postId, User user) {
        Post post = postService.findById(postId);
        userService.findByUsername(user.getUsername());

        PostVote postVote = postVoteRepository.findByPost(post);
        List<String> optionTextList = voteOptionRepository.findAllByPostVote(postVote).stream().map(VoteOption::getOptionText).toList();

        return new VoteResponseDto(postVote,optionTextList);
    }

    public VoteResponseDto editPostVote(Long voteId, VoteRequestDto voteRequestDto, User user) {
        PostVote postVote = findById(voteId);
        User loginUser = userService.findByUsername(user.getUsername());
        if(postVote.getPost().getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        postVote.updateVote(voteRequestDto);

        voteOptionRepository.deleteAllByPostVote(postVote);

        List<VoteOption> updatedVoteOptions = voteRequestDto.getVoteOption().stream().map(optionText -> new VoteOption(postVote,optionText)).toList();
        voteOptionRepository.saveAll(updatedVoteOptions);

        List<String> optionTextList = updatedVoteOptions.stream().map(VoteOption::getOptionText).toList();

        return new VoteResponseDto(postVote,optionTextList);
    }

    public void deletePostVote(Long voteId, User user) {
        PostVote postVote = findById(voteId);
        User loginUser = userService.findByUsername(user.getUsername());
        if(postVote.getPost().getUser()!=loginUser){
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }
        postVoteRepository.delete(postVote);
    }

    public PostVote findById(Long voteId){
        return postVoteRepository.findById(voteId).orElseThrow(
            ()->new CustomException(ErrorCode.BAD_REQUEST)
        );
    }
}
