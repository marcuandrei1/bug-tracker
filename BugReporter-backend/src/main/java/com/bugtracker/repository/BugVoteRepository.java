package com.bugtracker.repository;

import com.bugtracker.entity.BugVote;
import com.bugtracker.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BugVoteRepository extends JpaRepository<BugVote, Long> {

    Optional<BugVote> findByUserIdAndBugId(Long userId, Long bugId);

    long countByBugIdAndVoteType(Long bugId, VoteType voteType);
}
