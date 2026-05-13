package com.bugtracker.service;

import com.bugtracker.entity.VoteType;
import com.bugtracker.repository.BugVoteRepository;
import com.bugtracker.repository.CommentVoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserScoreServiceTest {

    @Mock
    private BugVoteRepository bugVoteRepository;
    @Mock
    private CommentVoteRepository commentVoteRepository;

    @InjectMocks
    private UserScoreService userScoreService;

    @Test
    void shouldCalculateCorrectScore() {
        Long userId = 1L;

        // 2 like uri, 0 dislike uri
        when(bugVoteRepository.countByBugAuthorIdAndVoteType(userId, VoteType.LIKE)).thenReturn(2L);
        when(bugVoteRepository.countByBugAuthorIdAndVoteType(userId, VoteType.DISLIKE)).thenReturn(0L);

        // 1 comment like, 0 dislike
        when(commentVoteRepository.countByCommentAuthorIdAndVoteType(userId, VoteType.LIKE)).thenReturn(1L);
        when(commentVoteRepository.countByCommentAuthorIdAndVoteType(userId, VoteType.DISLIKE)).thenReturn(0L);

        // 0 dislike uri date
        when(commentVoteRepository.countByUserIdAndVoteTypeAndCommentAuthorIdNot(userId, VoteType.DISLIKE, userId)).thenReturn(0L);

        double score = userScoreService.calculateScore(userId);

        // (2 like uri * 2.5) + (1 comment like* 5.0) = 10.0
        assertEquals(10.0, score);
    }
}