package com.bugtracker.service;

import com.bugtracker.entity.*;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.BugVoteRepository;
import com.bugtracker.repository.TagRepository;
import com.bugtracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final BugVoteRepository bugVoteRepository;

    public BugService(BugRepository bugRepository, TagRepository tagRepository, UserRepository userRepository, BugVoteRepository bugVoteRepository) {
        this.bugRepository = bugRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.bugVoteRepository = bugVoteRepository;
    }

    public Bug createBug(Bug bug) {
        User author = userRepository.findById(bug.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        bug.setAuthor(author);
        bug.setStatus(BugStatus.RECEIVED);      // statis implicit la bug cand il cream

        // procesez tagurile, daca nu gasesc tag-urile introduse de utilizator atunci le creez in repo pe loc si dupaia le setez la bug
        if (bug.getTags() != null && !bug.getTags().isEmpty()) {
            List<Tag> resolvedTags = bug.getTags().stream()
                    .map(tag -> tagRepository.findByName(tag.getName())
                            .orElseGet(() -> tagRepository.save(tag)))
                    .toList();

            bug.setTags(resolvedTags);
        }

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

    public List<Bug> searchByTitle(String title) {
        return bugRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
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
        Bug bug = bugRepository.findById(id).orElseThrow();

        long likes = bugVoteRepository.countByBugIdAndVoteType(id, VoteType.LIKE);
        long dislikes = bugVoteRepository.countByBugIdAndVoteType(id, VoteType.DISLIKE);

        bug.setScore((int) (likes - dislikes));
        return bug;
    }


    // la updateBug, pentru lista de Tag-uri se poate modifica doar lista de taguri, nu sa modificam tagurile din interiorul unui bug
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

    public Bug markAsSolved(Long id) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
        bug.setStatus(BugStatus.SOLVED);
        return bugRepository.save(bug);
    }
}