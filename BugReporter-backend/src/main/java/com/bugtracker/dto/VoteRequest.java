package com.bugtracker.dto;

import com.bugtracker.entity.VoteType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequest {
    private Long userId;
    private VoteType voteType;
}
