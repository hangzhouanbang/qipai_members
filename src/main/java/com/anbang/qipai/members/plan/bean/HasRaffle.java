package com.anbang.qipai.members.plan.bean;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/5 4:16 PM
 * @Version 1.0
 */
public class HasRaffle {
    boolean isRaffleToday;
    String extraRaffle;

    public HasRaffle() {
        isRaffleToday = false;
    }

    public HasRaffle(boolean isRaffleToday, String extraRaffle) {
        this.isRaffleToday = isRaffleToday;
        this.extraRaffle = extraRaffle;
    }

    public boolean isRaffleToday() {
        return isRaffleToday;
    }

    public void setRaffleToday(boolean raffleToday) {
        isRaffleToday = raffleToday;
    }

    public String getExtraRaffle() {
        return extraRaffle;
    }

    public void setExtraRaffle(String extraRaffle) {
        this.extraRaffle = extraRaffle;
    }
}
