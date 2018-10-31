package com.anbang.qipai.members.cqrs.c.domain.prize;

public class RaffleHistory {
    private final String memberId;
    private final Lottery lottery;
    private final long time;
    private final boolean firstTime;

    public RaffleHistory(String memberId, Lottery lottery, long time, boolean firstTime) {
        this.memberId = memberId;
        this.lottery = lottery;
        this.time = time;
        this.firstTime = firstTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public long getTime() {
        return time;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    @Override
    public String toString() {
        return "RaffleHistory{" +
                "memberId='" + memberId + '\'' +
                ", lottery=" + lottery +
                ", time=" + time +
                ", firstTime=" + firstTime +
                '}';
    }
}
