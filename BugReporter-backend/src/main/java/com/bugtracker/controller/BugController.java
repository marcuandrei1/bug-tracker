package com.bugtracker.controller;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.Comment;
import com.bugtracker.entity.Role;
import com.bugtracker.service.BugService;
import com.bugtracker.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    private final BugService bugService;
    private final CommentService commentService;

    public BugController(BugService bugService, CommentService commentService) {
        this.bugService = bugService;
        this.commentService = commentService;
    }

    @PostMapping
    public Bug createBug(@RequestBody Bug bug) {
        return bugService.createBug(bug);
    }

    // functia care daca ii dam parametri (authorId, tags) tine cont de ei, daca nu tot se executa
    @GetMapping
    public List<Bug> getAllBugs(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String title) {

        if (title != null && !title.isEmpty()) {
            return bugService.searchByTitle(title);
        }

        if (authorId != null && tags != null && !tags.isEmpty()) {
            return bugService.getBugsByAuthorAndTags(authorId, tags.toArray(new String[0]));
        }

        if (authorId != null) {
            return bugService.getBugsByAuthor(authorId);
        }

        if (tags != null && !tags.isEmpty()) {
            return bugService.getBugsByTags(tags.toArray(new String[0]));
        }

        return bugService.getAllBugs();
    }

    @GetMapping("/{id}")
    public Bug getBugById(@PathVariable Long id, @RequestParam(required=false)Long userId) {
        return bugService.getBugById(id, userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bug> updateBug(
            @PathVariable Long id,
            @RequestBody Bug bug,
            @RequestHeader("X-Current-User-Id") Long currentUserId,
            @RequestHeader("X-Current-User-Role") String currentUserRole) {

        Role roleEnum = Role.valueOf(currentUserRole);

        Bug updatedBug = bugService.updateBug(id, bug, currentUserId, roleEnum);
        return ResponseEntity.ok(updatedBug);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBug(
            @PathVariable Long id,
            @RequestHeader("X-Current-User-Id") Long currentUserId,
            @RequestHeader("X-Current-User-Role") String currentUserRole) {

        Role roleEnum = Role.valueOf(currentUserRole);

        bugService.deleteBug(id, currentUserId, roleEnum);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bugId}/comments")
    public List<Comment> getCommentsByBugId(@PathVariable Long bugId, @RequestParam(required=false)Long userId) {
        return commentService.getCommentsByBugId(bugId, userId);
    }

    @PutMapping("/{id}/solve")
    public Bug markAsSolved(@PathVariable Long id) {
        return bugService.markAsSolved(id);
    }
}