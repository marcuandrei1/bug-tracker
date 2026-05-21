package com.bugtracker.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@ToString(exclude = {"author", "bug"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "bug_id", nullable = false)
    private Bug bug;

    @Transient
    private int score;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private java.util.List<CommentVote> votes = new java.util.ArrayList<>();

    @Transient
    private String userVoteType;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}