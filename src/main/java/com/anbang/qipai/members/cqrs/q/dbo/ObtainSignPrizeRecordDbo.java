package com.anbang.qipai.members.cqrs.q.dbo;

import com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum;

public class ObtainSignPrizeRecordDbo {
    private String memberId;
    private PrizeEnum prize;
    private long time;

    public ObtainSignPrizeRecordDbo(String memberId, PrizeEnum prize, long time) {
        this.memberId = memberId;
        this.prize = prize;
        this.time = time;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public PrizeEnum getPrize() {
        return prize;
    }

    public void setPrize(PrizeEnum prize) {
        this.prize = prize;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
