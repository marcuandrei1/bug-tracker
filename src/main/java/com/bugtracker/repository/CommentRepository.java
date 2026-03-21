package com.bugtracker.repository;

import com.bugtracker.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBugId(Long bugId);      // cand afisez un bug individual, trebuie sa afisez si lista de commenturi
}