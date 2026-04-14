package com.bugtracker.repository;

import com.bugtracker.entity.CommentVote;
import com.bugtracker.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    Optional<CommentVote> findByUserIdAndCommentId(Long userId, Long commentId);

    long countByCommentIdAndVoteType(Long commentId, VoteType voteType);
}