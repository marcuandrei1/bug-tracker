package com.bugtracker.controller;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.Comment;
import com.bugtracker.service.BugService;
import com.bugtracker.service.CommentService;
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
    public List<Bug> getAllBugs(@RequestParam(required = false) Long authorId, @RequestParam(required = false) List<String> tags) {
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
    public Bug getBugById(@PathVariable Long id) {
        return bugService.getBugById(id);
    }

    @PutMapping("/{id}")
    public Bug updateBug(@PathVariable Long id, @RequestBody Bug bug) {
        return bugService.updateBug(id, bug);
    }

    @DeleteMapping("/{id}")
    public void deleteBug(@PathVariable Long id) {
        bugService.deleteBug(id);
    }

    @GetMapping("/{bugId}/comments")
    public List<Comment> getCommentsByBugId(@PathVariable Long bugId) {
        return commentService.getCommentsByBugId(bugId);
    }
}