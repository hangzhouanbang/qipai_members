package com.anbang.qipai.members.cqrs.c.domain.sign;

import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;

public class SignHistory {
    private final String memberId;
    private final long time;
    private final int continuousSignDays;
    private final VIPEnum vipEnum;

    public SignHistory(String memberId, long time, int continuousSignDays, VIPEnum vipEnum) {
        this.memberId = memberId;
        this.time = time;
        this.continuousSignDays = continuousSignDays;
        this.vipEnum = vipEnum;
    }

    public String getMemberId() {
        return memberId;
    }

    public long getTime() {
        return time;
    }

    public int getContinuousSignDays() {
        return continuousSignDays;
    }

    public VIPEnum getVipEnum() {
        return vipEnum;
    }
}
