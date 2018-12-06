package com.anbang.qipai.members.cqrs.q.dbo;

import com.anbang.qipai.members.enums.ExtraRaffle;
import com.anbang.qipai.members.plan.bean.Address;

public class MemberRaffleHistoryDbo {
    private String id;
    private String memberId;
    private Lottery lottery;
    private Address address;
    private long time;
    private boolean firstTime;
    private String extraRaffle;

    public MemberRaffleHistoryDbo() {
    }

    public MemberRaffleHistoryDbo(String id, String memberId, Lottery lottery, Address address, long time, boolean firstTime) {
        this.id = id;
        this.memberId = memberId;
        this.lottery = lottery;
        this.time = time;
        this.firstTime = firstTime;
        this.extraRaffle = "NO";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
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

    public String getExtraRaffle() {
        return extraRaffle;
    }

    public void setExtraRaffle(String extraRaffle) {
        this.extraRaffle = extraRaffle;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "MemberRaffleHistoryDbo{" +
                "id='" + id + '\'' +
                ", memberId='" + memberId + '\'' +
                ", lottery=" + lottery +
                ", address=" + address +
                ", time=" + time +
                ", firstTime=" + firstTime +
                ", extraRaffle='" + extraRaffle + '\'' +
                '}';
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
