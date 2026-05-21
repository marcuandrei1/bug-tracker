package com.bugtracker.controller;

import com.bugtracker.entity.Role;
import com.bugtracker.entity.User;
import com.bugtracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User created = userService.register(user);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            User user = userService.login(body.get("username"), body.get("password"));
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Optional<List<User>>> searchUsers(
            @RequestParam String username,
            @RequestHeader("X-Current-User-Role") Role currentUserRole) {

        if (currentUserRole != Role.MODERATOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<List<User>> users = userService.searchUsersByUsername(username);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/ban")
    public ResponseEntity<User> banUser(
            @PathVariable Long id,
            @RequestHeader("X-Current-User-Role") String currentUserRole) {

        if (!"MODERATOR".equals(currentUserRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User bannedUser = userService.banUser(id);
        return ResponseEntity.ok(bannedUser);
    }

    @PutMapping("/{id}/unban")
    public ResponseEntity<User> unbanUser(
            @PathVariable Long id,
            @RequestHeader("X-Current-User-Role") Role currentUserRole) {
        if (currentUserRole != Role.MODERATOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userService.unbanUser(id));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}