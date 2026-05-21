package com.statsservice.service;

import com.statsservice.dto.StatsResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final JdbcTemplate jdbc;

    public StatsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public StatsResponse getStats() {
        long totalBugs     = count("SELECT COUNT(*) FROM bugtracker.bug");
        long received      = count("SELECT COUNT(*) FROM bugtracker.bug WHERE status = 'RECEIVED'");
        long inProgress    = count("SELECT COUNT(*) FROM bugtracker.bug WHERE status = 'IN_PROGRESS'");
        long solved        = count("SELECT COUNT(*) FROM bugtracker.bug WHERE status = 'SOLVED'");
        long totalComments = count("SELECT COUNT(*) FROM bugtracker.comment");

        long totalUsers    = count("SELECT COUNT(*) FROM bugtracker.users");

        return new StatsResponse(totalBugs, received, inProgress, solved, totalComments, totalUsers);
    }

    private long count(String sql) {
        Long result = jdbc.queryForObject(sql, Long.class);
        return result != null ? result : 0L;
    }
}
