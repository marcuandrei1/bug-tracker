package com.bugtracker.service;

import com.bugtracker.entity.Comment;
import com.bugtracker.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {     // nu stiu daca vom avea nevoie neaparat de getAllComments si getCommentsById pentru ca merg impreuna cu buggurile
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {    // nici de asta nu stiu exact daca avem nevoie
        return commentRepository.findById(id).get();
    }

    public List<Comment> getCommentsByBugId(Long bugId) {
        return commentRepository.findByBugId(bugId);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (updatedComment.getText() != null) {
            comment.setText(updatedComment.getText());
        }

        if (updatedComment.getImageUrl() != null) {
            comment.setImageUrl(updatedComment.getImageUrl());
        }

        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}