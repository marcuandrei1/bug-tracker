package com.bugtracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"author", "tags"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BugStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToMany
    @JoinTable(
            name = "bug_tags",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}