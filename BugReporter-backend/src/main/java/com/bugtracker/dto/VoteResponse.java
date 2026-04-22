package com.bugtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteResponse {
    private int score;
    private String userVoteType;
}
