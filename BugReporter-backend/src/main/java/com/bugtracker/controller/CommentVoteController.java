package com.bugtracker.controller;

import com.bugtracker.dto.VoteRequest;
import com.bugtracker.dto.VoteResponse;
import com.bugtracker.service.CommentVoteService;
import com.bugtracker.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentVoteController {

    private final CommentVoteService commentVoteService;
    private final UserService userService;

    public CommentVoteController(CommentVoteService commentVoteService, UserService userService) {
        this.commentVoteService = commentVoteService;
        this.userService = userService;
    }

    @PostMapping("/{commentId}/vote")
    public VoteResponse voteComment(@PathVariable Long commentId, @RequestBody VoteRequest request) {
        return commentVoteService.vote(commentId, request.getUserId(), request.getVoteType());
    }
}
