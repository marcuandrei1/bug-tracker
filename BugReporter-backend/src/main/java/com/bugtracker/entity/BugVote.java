package com.bugtracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "bug_votes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "bug_id"})
        }
)
@NoArgsConstructor
@Getter
@Setter
public class BugVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bug_id", nullable = false)
    private Bug bug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;


    public BugVote(User user, Bug bug, VoteType voteType) {
        this.user = user;
        this.bug = bug;
        this.voteType = voteType;
    }
}
