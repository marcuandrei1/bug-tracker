package com.bugtracker.service;

import com.bugtracker.entity.Bug;
import com.bugtracker.entity.BugVote;
import com.bugtracker.entity.User;
import com.bugtracker.entity.VoteType;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.BugVoteRepository;
import com.bugtracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BugVoteService {

    private final BugVoteRepository bugVoteRepository;
    private final BugRepository bugRepository;
    private final UserRepository userRepository;

    public BugVoteService(BugVoteRepository bugVoteRepository, BugRepository bugRepository, UserRepository userRepository) {
        this.bugVoteRepository = bugVoteRepository;
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
    }

    public int vote(Long bugId, Long userId, VoteType voteType) {

        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verificarea de a nu te vota singur
        if (bug.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You cannot vote your own bug");
        }

        // verific daca exista vot
        BugVote existingVote = bugVoteRepository
                .findByUserIdAndBugId(userId, bugId)
                .orElse(null);

        if (existingVote == null) {
            // daca nu exista, creez vot nou
            BugVote vote = new BugVote(user, bug, voteType);
            bugVoteRepository.save(vote);

        } else if (existingVote.getVoteType() == voteType) {
            // acelasi vot (userul apasa din nou LIKE, atunci scot votul)
            bugVoteRepository.delete(existingVote);

        } else {
            // exista dar e diferit (de exemplu vreau sa schimb din LIKE in DISLIKE)
            existingVote.setVoteType(voteType);
            bugVoteRepository.save(existingVote);
        }

        // calculez scorul
        long likes = bugVoteRepository.countByBugIdAndVoteType(bugId, VoteType.LIKE);
        long dislikes = bugVoteRepository.countByBugIdAndVoteType(bugId, VoteType.DISLIKE);

        return (int) (likes - dislikes);
    }
}
