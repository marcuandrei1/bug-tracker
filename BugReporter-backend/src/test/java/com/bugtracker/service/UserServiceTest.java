package com.bugtracker.service;

import com.bugtracker.entity.User;
import com.bugtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("test_user");
        user.setEmail("test@test.com");
        user.setPassword("pass");
    }

    @Test
    void shouldRegisterUser() {
        when(userRepository.findByUsername("test_user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded_pass");
        when(userRepository.save(user)).thenReturn(user);

        User created = userService.register(user);

        assertNotNull(created);
        assertEquals("test_user", created.getUsername());
        verify(passwordEncoder).encode("pass");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowWhenUsernameExists() {
        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.register(user));
    }

    @Test
    void shouldThrowWhenEmailExists() {
        when(userRepository.findByUsername("test_user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.register(user));
    }

    @Test
    void shouldLoginUser() {
        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "pass")).thenReturn(true);

        User loggedIn = userService.login("test_user", "pass");

        assertEquals("test_user", loggedIn.getUsername());
    }

    @Test
    void shouldThrowWhenLoginWrongPassword() {
        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "pass")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login("test_user", "wrong"));
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.getUserById(1L);

        assertEquals("test_user", found.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(2L);
        });
    }

    @Test
    void shouldUpdateUser() {
        User updated = new User();
        updated.setUsername("updated_user");
        updated.setEmail("updated@test.com");
        updated.setPassword("newpass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encoded_newpass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, updated);

        assertEquals("updated_user", result.getUsername());
        assertEquals("updated@test.com", result.getEmail());
        verify(passwordEncoder).encode("newpass");
        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}