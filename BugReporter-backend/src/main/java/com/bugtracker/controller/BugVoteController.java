package com.bugtracker.controller;

import com.bugtracker.dto.VoteRequest;
import com.bugtracker.dto.VoteResponse;
import com.bugtracker.service.BugVoteService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bugs")
public class BugVoteController {

    private final BugVoteService bugVoteService;

    public BugVoteController(BugVoteService bugVoteService) {
        this.bugVoteService = bugVoteService;
    }

    @PostMapping("/{bugId}/vote")
    public VoteResponse voteBug(@PathVariable Long bugId, @RequestBody VoteRequest request) {
        return bugVoteService.vote(bugId, request.getUserId(), request.getVoteType());
    }
}
