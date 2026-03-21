package com.bugtracker.repository;

import com.bugtracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser(){
        User user=new User();
        user.setUsername("save_user");
        user.setEmail("save_user@test.com");
        user.setPassword("pass");

        User savedUser=userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());
        assertEquals("save_user", savedUser.getUsername());
        assertEquals("save_user@test.com", savedUser.getEmail());
    }

    @Test
    void shouldFindByUsername(){
        User user=new User();
        user.setUsername("find_user");
        user.setEmail("find_user@test.com");
        user.setPassword("pass");
        userRepository.save(user);

        Optional<User> found=userRepository.findByUsername("find_user");

        assertTrue(found.isPresent());
        assertEquals("find_user@test.com", found.get().getEmail());
    }
    @Test
    void shouldFindByEmail(){
        User user=new User();
        user.setUsername("find_email");
        user.setEmail("find_email@test.com");
        user.setPassword("pass");
        userRepository.save(user);

        Optional<User> found=userRepository.findByEmail("find_email@test.com");

        assertTrue(found.isPresent());
        assertEquals("find_email", found.get().getUsername());
    }

    @Test
    void shouldNotAllowDuplicateEmail() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("duplicate@test.com");
        user1.setPassword("pass");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("duplicate@test.com");
        user2.setPassword("pass");

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }
}
