package com.anbang.qipai.members.cqrs.c.domain.prize;

public class RaffleHistoryValueObject {

    private final String memberId;
    private final LotteryValueObject lottery;
    private final long time;
    private final boolean firstTime;

    public RaffleHistoryValueObject(RaffleHistory raffleHistory) {
        this.memberId = raffleHistory.getMemberId();
        this.lottery = new LotteryValueObject(raffleHistory.getLottery());
        this.time = raffleHistory.getTime();
        this.firstTime = raffleHistory.isFirstTime();
    }

    public String getMemberId() {
        return memberId;
    }

    public LotteryValueObject getLottery() {
        return lottery;
    }

    public long getTime() {
        return time;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

}
