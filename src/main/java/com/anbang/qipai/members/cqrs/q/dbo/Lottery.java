package com.anbang.qipai.members.cqrs.q.dbo;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;

public class Lottery {
    private String id;
    private String name;
    private int prop;
    private int firstPop;
    private LotteryTypeEnum type;
    private int singleNum;

    public Lottery() {
    }

    public Lottery(String id, String name, int prop, int firstPop, LotteryTypeEnum type, int singleNum) {
        this.id = id;
        this.name = name;
        this.prop = prop;
        this.firstPop = firstPop;
        this.type = type;
        this.singleNum = singleNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProp() {
        return prop;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }

    public int getFirstPop() {
        return firstPop;
    }

    public void setFirstPop(int firstPop) {
        this.firstPop = firstPop;
    }

    public LotteryTypeEnum getType() {
        return type;
    }

    public void setType(LotteryTypeEnum type) {
        this.type = type;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }
}
