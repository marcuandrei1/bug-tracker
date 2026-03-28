package com.bugtracker.service;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugStatus;
import com.bugtracker.repository.BugRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    /**
     * Metoda getAllBugs returneaza deja in ordine descrescatoare bugg-urile dupa campul createdAt
     * */
    public List<Bug> getAllBugs() {
        return bugRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Bug> getBugsByAuthor(Long authorId) {
        return bugRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }

    public List<Bug> getBugsByTags(String... tagNames) {
        if (tagNames == null || tagNames.length == 0) {
            return bugRepository.findAllByOrderByCreatedAtDesc();
        }

        return bugRepository.findDistinctByTags_NameInOrderByCreatedAtDesc(Arrays.asList(tagNames));
    }

    public List<Bug> getBugsByAuthorAndTags(Long authorId, String... tagNames) {
        if (tagNames == null || tagNames.length == 0) {
            return bugRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
        }

        return bugRepository.findByAuthorIdAndTags_NameInOrderByCreatedAtDesc(
                authorId,
                Arrays.asList(tagNames)
        );
    }

    public Bug getBugById(Long id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
    }

    public Bug updateBug(Long id, Bug updatedBug) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        if (updatedBug.getTitle() != null) {
            bug.setTitle(updatedBug.getTitle());
        }
        if (updatedBug.getText() != null) {
            bug.setText(updatedBug.getText());
        }
        if (updatedBug.getImageUrl() != null) {
            bug.setImageUrl(updatedBug.getImageUrl());
        }
        if (updatedBug.getStatus() != null) {
            bug.setStatus(updatedBug.getStatus());
        }
        if (updatedBug.getTags() != null) {
            bug.setTags(updatedBug.getTags());
        }

        return bugRepository.save(bug);
    }

    public void deleteBug(Long id) {
        bugRepository.deleteById(id);
    }
}