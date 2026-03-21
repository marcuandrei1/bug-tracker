package com.bugtracker.repository;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.entity.Tag;
import com.bugtracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BugRepositoryTest {
    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldSaveBugWithAuthor(){
        // Creeaza un User author
        User author=new User();
        author.setUsername("test_author");
        author.setEmail("test_author@test.com");
        author.setPassword("pass");
        userRepository.save(author);

        // Creeaza un Bug pentru author
        Bug bug=new Bug();
        bug.setTitle("Test Bug");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(author);
        Bug savedBug=bugRepository.save(bug);

        // Verifica daca e salvat corect
        assertNotNull(savedBug.getId());
        assertNotNull(savedBug.getCreatedAt());
        assertEquals("test_author", savedBug.getAuthor().getUsername());
    }

    @Test
    void shouldFailSavingBugWithoutAuthor() {
        Bug bug = new Bug();
        bug.setTitle("Test Bug - No author");
        bug.setText("Bug without an author.");
        bug.setStatus(BugStatus.RECEIVED);
        // bug.setAuthor(null);

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            bugRepository.saveAndFlush(bug);
        });
    }

    @Test
    void shouldSaveBugWithTags() {
        User author = new User();
        author.setUsername("test_tag");
        author.setEmail("test_tag@test.com");
        author.setPassword("pass");
        userRepository.save(author);

        Tag tag1 = new Tag();
        tag1.setName("URGENT");
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName("FRONTEND");
        tagRepository.save(tag2);

        Bug bug = new Bug();
        bug.setTitle("Test Bug - Tag");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(author);
        bug.setTags(List.of(tag1, tag2));

        Bug savedBug = bugRepository.save(bug);

        assertNotNull(savedBug.getId());
        assertEquals(2, savedBug.getTags().size());
        assertTrue(savedBug.getTags().stream().anyMatch(t -> t.getName().equals("URGENT")));
    }
}
