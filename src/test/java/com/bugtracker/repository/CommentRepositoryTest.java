package com.bugtracker.repository;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.entity.Comment;
import com.bugtracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CommentRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldSaveCommentWithAuthorAndBug(){
        User user = new User();
        user.setUsername("test_comment");
        user.setEmail("test_comment@test.com");
        user.setPassword("pass");
        userRepository.save(user);

        Bug bug = new Bug();
        bug.setTitle("Test Bug - Comment");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(user);
        bugRepository.save(bug);

        Comment comment = new Comment();
        comment.setText("Comment for the bug...");
        comment.setAuthor(user);
        comment.setBug(bug);

        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment.getId());
        assertNotNull(savedComment.getCreatedAt());
        assertEquals("test_comment", savedComment.getAuthor().getUsername());
        assertEquals(bug.getId(), savedComment.getBug().getId());
    }
}
