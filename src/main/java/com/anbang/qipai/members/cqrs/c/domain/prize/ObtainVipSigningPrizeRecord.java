package com.anbang.qipai.members.cqrs.c.domain.prize;

import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;

public class ObtainVipSigningPrizeRecord {
    private String memberId;
    private VIPEnum vipLevel;
    private VipPrizeEnum vipPrize;
    private long obtinTime;

    public ObtainVipSigningPrizeRecord(String memberId, VIPEnum vipLevel, VipPrizeEnum vipPrize, long obtinTime) {
        this.memberId = memberId;
        this.vipLevel = vipLevel;
        this.vipPrize = vipPrize;
        this.obtinTime = obtinTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public VIPEnum getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(VIPEnum vipLevel) {
        this.vipLevel = vipLevel;
    }

    public VipPrizeEnum getVipPrize() {
        return vipPrize;
    }

    public void setVipPrize(VipPrizeEnum vipPrize) {
        this.vipPrize = vipPrize;
    }

    public long getObtinTime() {
        return obtinTime;
    }

    public void setObtinTime(long obtinTime) {
        this.obtinTime = obtinTime;
    }



}
