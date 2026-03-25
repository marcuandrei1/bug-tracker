package com.bugtracker.repository;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.entity.Tag;
import com.bugtracker.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BugRepositoryTest {
    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private User author;

    // Folosim asta ca sa nu repetam aceeasi bucata de cod de fiecare data, in fiecare test
    @BeforeEach
    void setUp() {
        author = new User();
        author.setUsername("test_author");
        author.setEmail("test_author@test.com");
        author.setPassword("pass");
        userRepository.save(author);
    }

    @Test
    void shouldSaveBugWithAuthor(){
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

    @Test
    void shouldFindBugById() {
        Bug bug = new Bug();
        bug.setTitle("Test Bug - Find by ID");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(author);
        Bug saved = bugRepository.save(bug);

        Bug found = bugRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test Bug - Find by ID", found.getTitle());
    }

    @Test
    void shouldUpdateBugStatus(){
        Bug bug = new Bug();
        bug.setTitle("Test Bug - Find by ID");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(author);
        bugRepository.save(bug);

        bug.setStatus(BugStatus.IN_PROGRESS);
        Bug updated = bugRepository.save(bug);

        assertEquals(BugStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void shouldDeleteBug(){
        Bug bug = new Bug();
        bug.setTitle("Test Bug - Delete bug");
        bug.setText("Bug description...");
        bug.setStatus(BugStatus.RECEIVED);
        bug.setAuthor(author);
        bugRepository.save(bug);

        bugRepository.delete(bug);
        assertFalse(bugRepository.findById(bug.getId()).isPresent());
    }
}
