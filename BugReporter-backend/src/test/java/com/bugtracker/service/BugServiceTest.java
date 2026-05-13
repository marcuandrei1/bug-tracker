package com.bugtracker.service;

import com.bugtracker.entity.*;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.BugVoteRepository;
import com.bugtracker.repository.TagRepository;
import com.bugtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BugServiceTest {
    @Mock
    private BugRepository bugRepository;

    @Mock
    private BugVoteRepository bugVoteRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserScoreService userScoreService;

    @InjectMocks
    private BugService bugService;
    private Bug bug;
    private User author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
        author.setId(1L);
        author.setUsername("author");
        author.setEmail("author@test.com");

        bug = new Bug();
        bug.setTitle("Sample Bug");
        bug.setText("Bug description");
        bug.setAuthor(author);
        bug.setStatus(BugStatus.RECEIVED);
    }

    @Test
    void shouldCreateBug() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bugRepository.save(bug)).thenReturn(bug);

        Bug created = bugService.createBug(bug);

        assertNotNull(created);
        assertEquals(BugStatus.RECEIVED, created.getStatus());
        verify(bugRepository, times(1)).save(bug);
    }

    @Test
    void shouldGetAllBugs() {
        when(bugRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(bug));
        when(userScoreService.calculateScore(1L)).thenReturn(100.0);

        List<Bug> bugs = bugService.getAllBugs();

        assertEquals(1, bugs.size());
        assertEquals("Sample Bug", bugs.get(0).getTitle());
        assertEquals(100.0, bugs.get(0).getAuthor().getScore());
        verify(bugRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldGetBugByIdWithScore() {
        Long userId=1L, bugId=1L;
        when(bugRepository.findById(1L)).thenReturn(Optional.of(bug));

        // simuleaza faptul ca in baza de date avem 2 likeuri si 1 dislike pentru bugul cu id 1
        when(bugVoteRepository.countByBugIdAndVoteType(bugId, VoteType.LIKE)).thenReturn(2L);
        when(bugVoteRepository.countByBugIdAndVoteType(bugId, VoteType.DISLIKE)).thenReturn(1L);

        // simuleaza ca userul cu id 1 a votat deja bugul 1 cu LIKE
        BugVote vote= new BugVote(author, bug, VoteType.LIKE);
        when(bugVoteRepository.findByUserIdAndBugId(userId,bugId)).thenReturn(Optional.of(vote));

        Bug found = bugService.getBugById(1L,userId);

        // verificam daca scorul este 1 si tipul votului este like
        assertEquals(1, found.getScore());
        assertEquals("LIKE",found.getUserVoteType());
    }

    @Test
    void shouldThrowWhenBugNotFound() {
        when(bugRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bugService.getBugById(2L));
        assertEquals("No value present", ex.getMessage());
        verify(bugRepository, times(1)).findById(2L);
    }

    @Test
    void shouldUpdateBug() {
        Bug updatedBug = new Bug();
        updatedBug.setTitle("Updated Title");
        updatedBug.setText("Updated text");
        updatedBug.setStatus(BugStatus.IN_PROGRESS);

        when(bugRepository.findById(1L)).thenReturn(Optional.of(bug));
        when(bugRepository.save(any(Bug.class))).thenReturn(bug);

        Bug result = bugService.updateBug(1L, updatedBug);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated text", result.getText());
        assertEquals(BugStatus.IN_PROGRESS, result.getStatus());
        verify(bugRepository, times(1)).findById(1L);
        verify(bugRepository, times(1)).save(bug);
    }

    @Test
    void shouldDeleteBug() {
        doNothing().when(bugRepository).deleteById(1L);

        bugService.deleteBug(1L);

        verify(bugRepository, times(1)).deleteById(1L);
    }
}