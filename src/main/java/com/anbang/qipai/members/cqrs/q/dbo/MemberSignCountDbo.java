package com.anbang.qipai.members.cqrs.q.dbo;

import org.springframework.data.annotation.Id;

public class MemberSignCountDbo {

    @Id
    private String memberId;
    /**
     * 连续签到天数
     */
    private int days;

    private long lastSignTime;

    public MemberSignCountDbo() {
    }

    public MemberSignCountDbo(String memberId, int days, long lastSignTime) {
        this.memberId = memberId;
        this.days = days;
        this.lastSignTime = lastSignTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getLastSignTime() {
        return lastSignTime;
    }

    public void setLastSignTime(long lastSignTime) {
        this.lastSignTime = lastSignTime;
    }
}
