package com.bugtracker.repository;

import com.bugtracker.entity.CommentVote;
import com.bugtracker.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    Optional<CommentVote> findByUserIdAndCommentId(Long userId, Long commentId);

    long countByCommentIdAndVoteType(Long commentId, VoteType voteType);

    // like-uri si dislike-uri primite de comentariile userului
    long countByCommentAuthorIdAndVoteType(Long authorId, VoteType voteType);

    long countByUserIdAndVoteTypeAndCommentAuthorIdNot(Long userId, VoteType voteType, Long authorId);
}