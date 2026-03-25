package com.bugtracker.repository;

import com.bugtracker.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldNotAllowDuplicateTagName() {
        Tag tag1 = new Tag();
        tag1.setName("JAVA");
        tagRepository.saveAndFlush(tag1);

        Tag tag2 = new Tag();
        tag2.setName("JAVA");

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            tagRepository.saveAndFlush(tag2);
        });
    }

    @Test
    void shouldFindTagByName() {
        Tag tag = new Tag();
        tag.setName("SPRING");
        tagRepository.save(tag);

        Tag found = tagRepository.findByName("SPRING").orElse(null);

        assertNotNull(found);
        assertEquals("SPRING", found.getName());
    }
}
