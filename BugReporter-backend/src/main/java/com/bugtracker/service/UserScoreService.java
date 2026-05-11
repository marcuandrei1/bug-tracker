package com.bugtracker.service;

import com.bugtracker.entity.VoteType;
import com.bugtracker.repository.BugVoteRepository;
import com.bugtracker.repository.CommentVoteRepository;
import org.springframework.stereotype.Service;

@Service
public class UserScoreService {

    private final BugVoteRepository bugVoteRepository;
    private final CommentVoteRepository commentVoteRepository;

    public UserScoreService(
            BugVoteRepository bugVoteRepository,
            CommentVoteRepository commentVoteRepository
    ) {
        this.bugVoteRepository = bugVoteRepository;
        this.commentVoteRepository = commentVoteRepository;
    }

    public double calculateScore(Long userId) {
        long bugLikes = bugVoteRepository.countByBugAuthorIdAndVoteType(userId, VoteType.LIKE);
        long bugDislikes = bugVoteRepository.countByBugAuthorIdAndVoteType(userId, VoteType.DISLIKE);

        long commentLikes = commentVoteRepository.countByCommentAuthorIdAndVoteType(userId, VoteType.LIKE);
        long commentDislikes = commentVoteRepository.countByCommentAuthorIdAndVoteType(userId, VoteType.DISLIKE);

        long givenCommentDislikes = commentVoteRepository.countByUserIdAndVoteTypeAndCommentAuthorIdNot(
                userId,
                VoteType.DISLIKE,
                userId
        );

        return bugLikes * 2.5
                - bugDislikes * 1.5
                + commentLikes * 5.0
                - commentDislikes * 2.5
                - givenCommentDislikes * 1.5;
    }
}