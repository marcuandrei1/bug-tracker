package com.bugtracker.service;

import com.bugtracker.dto.VoteResponse;
import com.bugtracker.entity.*;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.BugVoteRepository;
import com.bugtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BugVoteServiceTest {

    @Mock
    private BugVoteRepository bugVoteRepository;
    @Mock
    private BugRepository bugRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BugVoteService bugVoteService;

    private User voter;
    private User author;
    private Bug bug;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
        author.setId(1L);

        voter = new User();
        voter.setId(2L);

        bug = new Bug();
        bug.setId(10L);
        bug.setAuthor(author);
    }

    @Test
    void shouldCreateNewVote() {
        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));
        when(userRepository.findById(2L)).thenReturn(Optional.of(voter));
        when(bugVoteRepository.findByUserIdAndBugId(2L, 10L)).thenReturn(Optional.empty());

        // se creeaza un vot de tipul LIKE
        VoteResponse response = bugVoteService.vote(10L, 2L, VoteType.LIKE);

        assertNotNull(response);
        verify(bugVoteRepository, times(1)).save(any(BugVote.class));
        verify(bugVoteRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteVoteWhenSameTypePressed() {
        BugVote existingVote = new BugVote(voter, bug, VoteType.LIKE);

        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));
        when(userRepository.findById(2L)).thenReturn(Optional.of(voter));
        when(bugVoteRepository.findByUserIdAndBugId(2L, 10L))
                .thenReturn(Optional.of(existingVote))
                .thenReturn(Optional.empty());

        // cand exista deja un vot si se voteaza iar, votul ar trebui sa fie anulat
        VoteResponse response = bugVoteService.vote(10L, 2L, VoteType.LIKE);

        verify(bugVoteRepository, times(1)).delete(existingVote);
        assertNull(response.getUserVoteType());
    }

    @Test
    void shouldUpdateVoteWhenDifferentTypePressed() {
        BugVote existingVote = new BugVote(voter, bug, VoteType.LIKE);

        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));
        when(userRepository.findById(2L)).thenReturn(Optional.of(voter));
        when(bugVoteRepository.findByUserIdAndBugId(2L, 10L)).thenReturn(Optional.of(existingVote));

        // votul este alternat din LIKE in DISLIKE
        VoteResponse response = bugVoteService.vote(10L, 2L, VoteType.DISLIKE);

        assertEquals(VoteType.DISLIKE, existingVote.getVoteType());
        verify(bugVoteRepository, times(1)).save(existingVote);
    }

    @Test
    void shouldThrowExceptionWhenAuthorVotesOwnBug() {
        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bugVoteService.vote(10L, 1L, VoteType.LIKE));

        assertEquals("You cannot vote your own bug", exception.getMessage());
    }
}