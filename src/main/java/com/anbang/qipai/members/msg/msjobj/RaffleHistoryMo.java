package com.anbang.qipai.members.msg.msjobj;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;

public class RaffleHistoryMo {
    private String id;
    private String lotteryId;
    private String memberId;
    private long time;
    private boolean firstTime;
    private LotteryTypeEnum lotteryTypeEnum;
    private int singleNum;

    public RaffleHistoryMo(String id, String lotteryId, String memberId, long time, boolean firstTime, LotteryTypeEnum lotteryTypeEnum, int singleNum) {
        this.id = id;
        this.lotteryId = lotteryId;
        this.memberId = memberId;
        this.time = time;
        this.firstTime = firstTime;
        this.lotteryTypeEnum = lotteryTypeEnum;
        this.singleNum = singleNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public LotteryTypeEnum getLotteryTypeEnum() {
        return lotteryTypeEnum;
    }

    public void setLotteryTypeEnum(LotteryTypeEnum lotteryTypeEnum) {
        this.lotteryTypeEnum = lotteryTypeEnum;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }
}
