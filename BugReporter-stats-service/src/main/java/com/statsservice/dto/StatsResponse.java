package com.statsservice.dto;

public class StatsResponse {

    private long totalBugs;
    private long received;
    private long inProgress;
    private long solved;
    private long totalComments;
    private long totalUsers;

    public StatsResponse(long totalBugs, long received, long inProgress,
                         long solved, long totalComments, long totalUsers) {
        this.totalBugs = totalBugs;
        this.received = received;
        this.inProgress = inProgress;
        this.solved = solved;
        this.totalComments = totalComments;
        this.totalUsers = totalUsers;
    }

    public long getTotalBugs()     { return totalBugs; }
    public long getReceived()      { return received; }
    public long getInProgress()    { return inProgress; }
    public long getSolved()        { return solved; }
    public long getTotalComments() { return totalComments; }
    public long getTotalUsers()    { return totalUsers; }
}
