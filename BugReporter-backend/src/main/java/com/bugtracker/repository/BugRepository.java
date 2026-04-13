package com.bugtracker.repository;

import com.bugtracker.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {

    List<Bug> findAllByOrderByCreatedAtDesc();

    List<Bug> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Bug> findDistinctByTags_NameInOrderByCreatedAtDesc(Collection<String> tagNames);

    List<Bug> findByAuthorIdAndTags_NameInOrderByCreatedAtDesc(Long authorId, Collection<String> tagNames);

    List<Bug> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
}