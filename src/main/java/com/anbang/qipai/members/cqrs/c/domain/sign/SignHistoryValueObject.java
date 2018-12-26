package com.anbang.qipai.members.cqrs.c.domain.sign;

public class SignHistoryValueObject {
    private final String memberId;
    private final long time;
    private int continuousSignDays;
    private final int vipLevel;

    public SignHistoryValueObject(SignHistory signHistory) {
        this.memberId = signHistory.getMemberId();
        this.time = signHistory.getTime();
        this.continuousSignDays = signHistory.getContinuousSignDays();
        this.vipLevel = signHistory.getVipEnum() == null ? 0 : signHistory.getVipEnum().getLevel();
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

    public int getVipLevel() {
        return vipLevel;
    }

    public void setContinuousSignDays(int continuousSignDays) {
        this.continuousSignDays = continuousSignDays;
    }
}
