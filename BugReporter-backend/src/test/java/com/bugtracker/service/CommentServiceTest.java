package com.bugtracker.service;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.entity.Comment;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BugRepository bugRepository;

    @InjectMocks
    private CommentService commentService;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
    }

    @Test
    void shouldCreateComment() {
        Bug bug= new Bug();
        bug.setId(10L);
        bug.setStatus(BugStatus.IN_PROGRESS);
        comment.setBug(bug);

        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment created = commentService.createComment(comment);

        assertNotNull(created);
        assertEquals("Test comment", created.getText());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void shouldGetCommentById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment found = commentService.getCommentById(1L);

        assertEquals("Test comment", found.getText());
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        when(commentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.getCommentById(2L));
    }

    @Test
    void shouldGetAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getAllComments();

        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getText());
    }

    @Test
    void shouldUpdateComment() {
        Comment updated = new Comment();
        updated.setText("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updated);

        Comment result = commentService.updateComment(1L, updated);

        assertEquals("Updated comment", result.getText());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void shouldDeleteComment() {
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldChangeBugStatusToInProgressOnFirstComment(){
        // cream un bug si ii atribuim un comentariu
        Bug bug=new Bug();
        bug.setId(10L);
        bug.setStatus(BugStatus.RECEIVED);
        comment.setBug(bug);

        when(commentRepository.save(comment)).thenReturn(comment);
        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));

        commentService.createComment(comment);

        assertEquals(BugStatus.IN_PROGRESS,bug.getStatus());
    }
}