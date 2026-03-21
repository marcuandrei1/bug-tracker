package com.bugtracker.repository;

import com.bugtracker.entity.Tag;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.stringprep.ProfileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
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
}
