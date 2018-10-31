package com.anbang.qipai.members.cqrs.c.domain.sign;

import com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum;

public class ObtainPrizeHistory {
    private final String memberId;
    private final PrizeEnum prize;
    private final long time;

    public ObtainPrizeHistory(String memberId, PrizeEnum prize, long time) {
        this.memberId = memberId;
        this.prize = prize;
        this.time = time;
    }

    public String getMemberId() {
        return memberId;
    }

    public PrizeEnum getPrize() {
        return prize;
    }

    public long getTime() {
        return time;
    }

}
