package com.bugtracker.repository;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.entity.Comment;
import com.bugtracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Bug bug;

    // Folosim asta ca sa nu repetam aceeasi bucata de cod de fiecare data, in fiecare test
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("test_comment_user");
        user.setEmail("test_comment_user@test.com");
        user.setPassword("pass");
        userRepository.save(user);

        bug = new Bug();
        bug.setTitle("Test Bug for Comments");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(user);
        bugRepository.save(bug);
    }

    @Test
    void shouldSaveCommentWithAuthorAndBug() {
        Comment comment = new Comment();
        comment.setText("This is a comment");
        comment.setAuthor(user);
        comment.setBug(bug);

        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment.getId());
        assertNotNull(savedComment.getCreatedAt());
        assertEquals(user.getUsername(), savedComment.getAuthor().getUsername());
        assertEquals(bug.getId(), savedComment.getBug().getId());
    }

    @Test
    void shouldFailSavingCommentWithoutAuthor() {
        Comment comment = new Comment();
        comment.setText("Comment without author");
        comment.setBug(bug);

        assertThrows(DataIntegrityViolationException.class, () -> {
            commentRepository.saveAndFlush(comment);
        });
    }

    @Test
    void shouldFailSavingCommentWithoutBug() {
        Comment comment = new Comment();
        comment.setText("Comment without bug");
        comment.setAuthor(user);

        assertThrows(DataIntegrityViolationException.class, () -> {
            commentRepository.saveAndFlush(comment);
        });
    }

    @Test
    void shouldUpdateCommentText() {
        Comment comment = new Comment();
        comment.setText("Original text");
        comment.setAuthor(user);
        comment.setBug(bug);
        Comment savedComment = commentRepository.save(comment);

        savedComment.setText("Updated text");
        Comment updatedComment = commentRepository.save(savedComment);

        assertEquals("Updated text", updatedComment.getText());
    }

    @Test
    void shouldDeleteComment() {
        Comment comment = new Comment();
        comment.setText("Comment to delete");
        comment.setAuthor(user);
        comment.setBug(bug);
        Comment savedComment = commentRepository.save(comment);

        commentRepository.delete(savedComment);

        assertFalse(commentRepository.findById(savedComment.getId()).isPresent());
    }

    @Test
    void shouldFindCommentsByBug() {
        Comment comment1 = new Comment();
        comment1.setText("First comment");
        comment1.setAuthor(user);
        comment1.setBug(bug);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Second comment");
        comment2.setAuthor(user);
        comment2.setBug(bug);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findByBugId(bug.getId());
        assertEquals(2, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("First comment")));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Second comment")));
    }
}