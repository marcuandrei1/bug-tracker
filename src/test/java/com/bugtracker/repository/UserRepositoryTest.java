package com.bugtracker.repository;

import com.bugtracker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @Test
    void shouldNotAllowDuplicateUsername() {
        User user1 = new User();
        user1.setUsername("user");
        user1.setEmail("email1@test.com");
        user1.setPassword("pass");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setUsername("user");
        user2.setEmail("email2@test.com");
        user2.setPassword("pass");

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    void shouldFindAllUsers() {
        User u1 = new User();
        u1.setUsername("u1");
        u1.setEmail("u1@test.com");
        u1.setPassword("pass");
        User u2 = new User();
        u2.setUsername("u2");
        u2.setEmail("u2@test.com");
        u2.setPassword("pass");
        userRepository.save(u1);
        userRepository.save(u2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void shouldDeleteUser() {
        User user = new User();
        user.setUsername("to_delete");
        user.setEmail("to_delete@test.com");
        user.setPassword("pass");
        userRepository.save(user);

        userRepository.delete(user);
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}
