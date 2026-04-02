package com.bugtracker.service;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.entity.User;
import com.bugtracker.repository.BugRepository;
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

    @InjectMocks
    private BugService bugService;
    private Bug bug;
    private User author;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
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
        when(bugRepository.save(bug)).thenReturn(bug);

        Bug created = bugService.createBug(bug);

        assertNotNull(created);
        assertEquals(BugStatus.RECEIVED, created.getStatus());
        verify(bugRepository, times(1)).save(bug);
    }

    @Test
    void shouldGetAllBugs() {
        when(bugRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(bug));

        List<Bug> bugs = bugService.getAllBugs();

        assertEquals(1, bugs.size());
        assertEquals("Sample Bug", bugs.get(0).getTitle());
        verify(bugRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldGetBugById() {
        when(bugRepository.findById(1L)).thenReturn(Optional.of(bug));

        Bug found = bugService.getBugById(1L);

        assertNotNull(found);
        assertEquals("Sample Bug", found.getTitle());
        verify(bugRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenBugNotFound() {
        when(bugRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bugService.getBugById(2L));
        assertEquals("Bug not found", ex.getMessage());
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