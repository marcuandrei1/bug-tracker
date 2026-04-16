package com.bugtracker.controller;

import com.bugtracker.dto.VoteRequest;
import com.bugtracker.dto.VoteResponse;
import com.bugtracker.service.CommentVoteService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentVoteController {

    private final CommentVoteService commentVoteService;

    public CommentVoteController(CommentVoteService commentVoteService) {
        this.commentVoteService = commentVoteService;
    }

    @PostMapping("/{commentId}/vote")
    public VoteResponse voteComment(@PathVariable Long commentId, @RequestBody VoteRequest request) {
        int score = commentVoteService.vote(commentId, request.getUserId(), request.getVoteType());
        return new VoteResponse(score);
    }
}
