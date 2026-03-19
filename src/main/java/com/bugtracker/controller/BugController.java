package com.bugtracker.controller;

import com.bugtracker.entity.Bug;
import com.bugtracker.service.BugService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @PostMapping
    public Bug createBug(@RequestBody Bug bug) {
        return bugService.createBug(bug);
    }

    @GetMapping
    public List<Bug> getAllBugs() {
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
}