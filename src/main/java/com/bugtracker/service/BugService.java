package com.bugtracker.service;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.repository.BugRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugService {

    private final BugRepository bugRepository;

    public BugService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    public Bug createBug(Bug bug) {
        bug.setStatus(BugStatus.RECEIVED); // setam status implicit
        return bugRepository.save(bug);
    }

    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public Bug getBugById(Long id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
    }

    public Bug updateBug(Long id, Bug updatedBug) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        bug.setTitle(updatedBug.getTitle());
        bug.setText(updatedBug.getText());
        bug.setImageUrl(updatedBug.getImageUrl());
        bug.setStatus(updatedBug.getStatus());

        return bugRepository.save(bug);
    }

    public void deleteBug(Long id) {
        bugRepository.deleteById(id);
    }
}