package com.statsservice.dto;

public record StatsResponse(long totalBugs, long received, long inProgress,
                            long solved, long totalComments, long totalUsers) {}