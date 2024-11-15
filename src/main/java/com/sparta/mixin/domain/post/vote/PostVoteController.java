package com.sparta.mixin.domain.post.vote;

import com.sparta.mixin.domain.post.vote.dto.VoteOptionResponseDto;
import com.sparta.mixin.domain.post.vote.dto.VoteRequestDto;
import com.sparta.mixin.domain.post.vote.dto.VoteResponseDto;
import com.sparta.mixin.domain.post.vote.entity.PostVote;
import com.sparta.mixin.global.common.CommonResponse;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import com.sparta.mixin.global.security.UserDetailsImpl;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/vote")
public class PostVoteController {
    private final PostVoteService postVoteService;

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<VoteResponseDto>> getPostVote(@PathVariable(name = "postId")Long postId,@AuthenticationPrincipal
        UserDetailsImpl userDetails){
        VoteResponseDto voteResponseDto = postVoteService.getPostVote(postId,userDetails.getUser());
        CommonResponse response = new CommonResponse("투표 조회 성공",200,voteResponseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<CommonResponse<VoteResponseDto>> editPostVote(@PathVariable(name = "voteId")Long voteId,@RequestBody VoteRequestDto voteRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        VoteResponseDto voteResponseDto = postVoteService.editPostVote(voteId,voteRequestDto,userDetails.getUser());
        CommonResponse response= new CommonResponse("투표 수정 성공",200,voteResponseDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<CommonResponse> deletePostVote(@PathVariable(name = "voteId")Long voteId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        postVoteService.deletePostVote(voteId,userDetails.getUser());
        CommonResponse response = new CommonResponse("투표 삭제 성공",200,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/{voteId}")
    public ResponseEntity<CommonResponse> submitVote(@PathVariable(name = "voteId")Long voteId,@RequestBody List<Long> voteOptionIds,@AuthenticationPrincipal UserDetailsImpl userDetails){
        PostVote postVote = postVoteService.findById(voteId);

        if(postVote.getDeadline().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.CLOSED_VOTE);
        }
        if(!postVote.isAllowMultipleVotes()&&voteOptionIds.size()>1){
            throw new CustomException(ErrorCode.NOT_ALLOW_MULTIPLE_VOTES);
        }
        postVoteService.submitVote(postVote,voteOptionIds,userDetails.getUser());
        CommonResponse response = new CommonResponse("투표 성공",200,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{voteId}/re")
    public ResponseEntity<CommonResponse> reSubmitVote(@PathVariable(name = "voteId")Long voteId,@RequestBody List<Long> voteOptionIds,@AuthenticationPrincipal UserDetailsImpl userDetails){
        PostVote postVote = postVoteService.findById(voteId);

        if(postVote.getDeadline().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.CLOSED_VOTE);
        }
        if(!postVote.isAllowMultipleVotes()&&voteOptionIds.size()>1){
            throw new CustomException(ErrorCode.NOT_ALLOW_MULTIPLE_VOTES);
        }
        postVoteService.reSubmitVote(postVote,voteOptionIds,userDetails.getUser());
        CommonResponse response = new CommonResponse("재투표 성공",200,"");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{voteId}/option")
    public ResponseEntity<CommonResponse<List<VoteOptionResponseDto>>> getAllVoteOption(@PathVariable(name = "voteId")Long voteId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<VoteOptionResponseDto> voteOptionResponseDtos = postVoteService.getAllVoteOption(voteId,userDetails.getUser());
        CommonResponse response = new CommonResponse("투표 항목 조회 성공",200,voteOptionResponseDtos);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
