package com.bugtracker.service;

import com.bugtracker.entity.Comment;
import com.bugtracker.entity.CommentVote;
import com.bugtracker.entity.User;
import com.bugtracker.entity.VoteType;
import com.bugtracker.repository.CommentRepository;
import com.bugtracker.repository.CommentVoteRepository;
import com.bugtracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentVoteService {

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentVoteService(CommentVoteRepository commentVoteRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.commentVoteRepository = commentVoteRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public int vote(Long commentId, Long userId, VoteType voteType) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // sa nu te votezi singur
        if (comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You cannot vote your own comment");
        }

        // verific daca exista vot
        CommentVote existingVote = commentVoteRepository
                .findByUserIdAndCommentId(userId, commentId)
                .orElse(null);

        if (existingVote == null) {
            // creez votul
            CommentVote vote = new CommentVote(user, comment, voteType);
            commentVoteRepository.save(vote);

        } else if (existingVote.getVoteType() == voteType) {
            // sa nu se puna voturi in plus
            commentVoteRepository.delete(existingVote);

        } else {
            // modific vot
            existingVote.setVoteType(voteType);
            commentVoteRepository.save(existingVote);
        }

        // calculez scor
        long likes = commentVoteRepository.countByCommentIdAndVoteType(commentId, VoteType.LIKE);
        long dislikes = commentVoteRepository.countByCommentIdAndVoteType(commentId, VoteType.DISLIKE);

        return (int) (likes - dislikes);
    }
}
